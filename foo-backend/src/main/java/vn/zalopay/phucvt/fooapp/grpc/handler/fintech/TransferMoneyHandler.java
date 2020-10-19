package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.*;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.grpc.exceptions.TransferMoneyException;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.*;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.time.Instant;
import java.util.List;

@Builder
@Log4j2
public class TransferMoneyHandler {
  private static final String METRIC = "TransferMoneyHandler";
  private final UserDA userDA;
  private final FintechDA fintechDA;
  private final FintechCache fintechCache;
  private final ChatDA chatDA;
  private final ChatCache chatCache;
  private final WSHandler wsHandler;
  private final TransactionProvider transactionProvider;

  public void handle(
      TransferMoneyRequest request, StreamObserver<TransferMoneyResponse> responseObserver) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    String userId = AuthInterceptor.USER_ID.get();
    log.info(
        "gRPC call: transferMoney (sender={}, receiver={}, amount={})",
        userId,
        request.getReceiverId(),
        request.getAmount());
    //    Init holder
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setTracker(tracker);
    getUserAuth(userId, holder)
        .compose(this::validateReceiverId)
        .compose(this::validatePassword)
        .compose(this::validateAmountTransfer)
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                TransferMoneyHolder validatedHolder = asyncResult.result();
                transferMoneyTransaction(responseObserver, validatedHolder);
              } else {
                log.error(
                    "validate preparation input failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
                handleExceptionResponse(asyncResult.cause(), responseObserver);
              }
            });
  }

  //  TRANSFER MONEY TRANSACTION
  private void transferMoneyTransaction(
      StreamObserver<TransferMoneyResponse> responseObserver, TransferMoneyHolder validatedHolder) {
    Transaction transaction = transactionProvider.newTransaction();
    validatedHolder.setTransaction(transaction);
    log.info(
        "start transfer money transaction (sender={}, receiver={}, amount={})",
        validatedHolder.getSender().getUserId(),
        validatedHolder.getRequest().getReceiverId(),
        validatedHolder.getRequest().getAmount());
    long startTime = System.nanoTime();
    transaction
        .begin()
        .compose(next -> checkBalance(validatedHolder))
        .compose(this::debit)
        .compose(this::credit)
        .compose(this::writeTransferLog)
        .compose(this::writeAccountLogSender)
        .compose(this::writeAccountLogReceiver)
        .setHandler(
            holderAsyncResult -> {
              if (holderAsyncResult.succeeded()) {
                TransferMoneyHolder holder = holderAsyncResult.result();
                handleSuccessTransferTransaction(responseObserver, startTime, holder);
              } else {
                transaction.rollback();
                transaction.close();
                log.error(
                    "transfer money transaction failed, cause={}",
                    ExceptionUtil.getDetail(holderAsyncResult.cause()));
                handleExceptionResponse(holderAsyncResult.cause(), responseObserver);
              }
            });
  }

  private void handleSuccessTransferTransaction(
      StreamObserver<TransferMoneyResponse> responseObserver,
      long startTime,
      TransferMoneyHolder holder) {
    holder.getTransaction().commit();
    long endTime = System.nanoTime();
    long duration = (endTime - startTime);
    log.info("Time execute a transfer money transaction: " + duration / 1000000); // for debug
    log.info(
        "End transfer money transaction (sender={}, receiver={}, amount={})",
        holder.getSender().getUserId(),
        holder.getRequest().getReceiverId(),
        holder.getRequest().getAmount());
    logTransferMessage(holder)
        .compose(this::appendToTransactionHistoryCache)
        .compose(this::updateLastMessageSender)
        .compose(this::updateLastMessagesAndUnseenMessagesReceiver)
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                notifyTransferMoney(holder);
                log.info(
                    "Transfer money successfully (sender={}, receiver={}, amount={})",
                    holder.getSender().getUserId(),
                    holder.getRequest().getReceiverId(),
                    holder.getRequest().getAmount());
                holder.getTransaction().close();
                TransferMoneyResponse response = buildSuccessTransferMoneyResponse(holder);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                holder.getTracker().code("ok").build().record();
              } else {
                log.error("remaining tasks after transaction failed, cause=", asyncResult.cause());
              }
            });
  }

  private void handleExceptionResponse(
      Throwable throwable, StreamObserver<TransferMoneyResponse> responseObserver) {
    Status status;
    if (throwable instanceof TransferMoneyException) {
      status = Status.newBuilder().setCode(((TransferMoneyException) throwable).getCode()).build();
    } else {
      status = Status.newBuilder().setCode(Code.INTERNAL).build();
    }
    TransferMoneyResponse response = TransferMoneyResponse.newBuilder().setStatus(status).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private TransferMoneyResponse buildSuccessTransferMoneyResponse(TransferMoneyHolder holder) {
    TransferMoneyResponse.Data data =
        TransferMoneyResponse.Data.newBuilder()
            .setBalance(holder.getSender().getBalance())
            .setLastUpdated(holder.getRecordedTime())
            .setTransaction(
                TransactionHistory.newBuilder()
                    .setUserId(holder.getRequest().getReceiverId())
                    .setAmount(holder.getRequest().getAmount())
                    .setDescription(holder.getRequest().getDescription())
                    .setTransferType(TransactionHistory.TransferType.SEND)
                    .build())
            .build();
    return TransferMoneyResponse.newBuilder()
        .setData(data)
        .setStatus(Status.newBuilder().setCode(Code.OK).build())
        .build();
  }

  //  Validation steps before start transaction
  private Future<TransferMoneyHolder> getUserAuth(String userId, TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    userDA
        .selectUserById(userId)
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                User userAuth = userAsyncResult.result();
                holder.setSender(userAuth);
                future.complete(holder);
              } else {
                future.fail(userAsyncResult.cause());
                log.error(
                    "get user auth failed, cause={}",
                    ExceptionUtil.getDetail(userAsyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> validateReceiverId(TransferMoneyHolder holder) {
    //    log.info("validate receiver id");
    Future<TransferMoneyHolder> future = Future.future();
    userDA
        .selectUserById(holder.getRequest().getReceiverId())
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                User u = userAsyncResult.result();
                if (u == null) {
                  future.fail(
                      new TransferMoneyException(
                          "receiver's id not found", Code.USER_ID_NOT_FOUND));
                } else {
                  future.complete(holder);
                }
              } else {
                future.fail("Get user auth failed");
                log.error(
                    "get user auth failed, cause={}",
                    ExceptionUtil.getDetail(userAsyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> validateAmountTransfer(TransferMoneyHolder holder) {
    //    log.info("validate amount transfer");
    Future<TransferMoneyHolder> future = Future.future();
    long amount = holder.getRequest().getAmount();
    if (amount < 1000 || amount > 20000000 || amount % 1000 != 0) {
      future.fail(new TransferMoneyException("Amount transfer invalid", Code.INVALID_INPUT_MONEY));
    } else {
      future.complete(holder);
    }
    return future;
  }

  public Future<TransferMoneyHolder> validatePassword(TransferMoneyHolder holder) {
    //    log.info("validate password");
    Future<TransferMoneyHolder> future = Future.future();
    User userAuth = holder.getSender();
    String confirmPassword = holder.getRequest().getConfirmPassword();
    if (BCrypt.checkpw(confirmPassword, userAuth.getPassword())) {
      future.complete(holder);
    } else {
      future.fail(new TransferMoneyException("Confirm password not match", Code.INVALID_PASSWORD));
    }
    return future;
  }

  //  Transaction steps
  public Future<TransferMoneyHolder> checkBalance(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    String userId = holder.getSender().getUserId();
    //    log.info(
    //        "transaction {}-{} , step : check balance", userId,
    // holder.getRequest().getReceiverId());
    Transaction transaction = holder.getTransaction();
    transaction
        .execute(fintechDA.selectUsersForUpdate(userId, holder.getRequest().getReceiverId()))
        .setHandler(
            listAsyncResult -> {
              if (listAsyncResult.succeeded()) {
                List<User> userList = listAsyncResult.result();
                User sender, receiver;
                if (userId.equals(userList.get(0).getUserId())) {
                  sender = userList.get(0);
                  receiver = userList.get(1);
                } else {
                  sender = userList.get(1);
                  receiver = userList.get(0);
                }
                if (sender.getBalance() >= holder.getRequest().getAmount()) {
                  holder.getSender().setBalance(sender.getBalance());
                  holder.setReceiver(receiver);
                  future.complete(holder);
                } else {
                  future.fail(
                      new TransferMoneyException(
                          "sender balance less than transferred amount", Code.NOT_ENOUGH_MONEY));
                }
              } else {
                future.fail(listAsyncResult.cause());
                log.error(
                    "get user balance failed, cause={}",
                    ExceptionUtil.getDetail(listAsyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> debit(TransferMoneyHolder holder) {
    //    log.info(
    //        "transaction {}-{}, step: debit sender's balance",
    //        holder.getSender().getUserId(),
    //        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String userId = holder.getSender().getUserId();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getSender().getBalance() - amount;
    long now = Instant.now().getEpochSecond();
    transaction
        .execute(fintechDA.updateBalance(userId, newBalance, now))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                holder.getSender().setBalance(newBalance);
                holder.setRecordedTime(now);
                future.complete(holder);
              } else {
                future.fail(asyncResult.cause());
                log.error(
                    "debit balance of user={} failed, cause={}",
                    userId,
                    ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> credit(TransferMoneyHolder holder) {
    //    log.info(
    //        "transaction {}-{}, step: credit sender's balance",
    //        holder.getSender().getUserId(),
    //        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getRequest().getReceiverId();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getReceiver().getBalance() + amount;
    long now = Instant.now().getEpochSecond();
    transaction
        .execute(fintechDA.updateBalance(receiverId, newBalance, now))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                holder.getReceiver().setBalance(newBalance);
                holder.setRecordedTime(Instant.now().getEpochSecond());
                future.complete(holder);
              } else {
                future.fail("Deposit userId=" + receiverId + " failed");
                log.error("deposit failed, cause={}", ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> writeTransferLog(TransferMoneyHolder holder) {
    //    log.info(
    //        "transaction {}-{}, step: write to transfer log",
    //        holder.getSender().getUserId(),
    //        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    Transfer transfer = buildTransferModel(holder);
    transaction
        .execute(fintechDA.insertTransferLog(transfer))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                holder.setTransferId(transfer.getTransferId());
                future.complete(holder);
              } else {
                future.fail(asyncResult.cause());
                log.error(
                    "log transfer {}-{} failed, cause={}",
                    holder.getSender().getUserId(),
                    holder.getReceiver().getUserId(),
                    ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  private Transfer buildTransferModel(TransferMoneyHolder holder) {
    return Transfer.builder()
        .transferId(GenerationUtils.generateId())
        .amount(holder.getRequest().getAmount())
        .sender(holder.getSender().getUserId())
        .receiver(holder.getRequest().getReceiverId())
        .description(holder.getRequest().getDescription())
        .recordedTime(holder.getRecordedTime())
        .build();
  }

  private Future<TransferMoneyHolder> writeAccountLogSender(TransferMoneyHolder holder) {
    //    log.info(
    //        "transaction {}-{}, step: write account log of sender",
    //        holder.getSender().getUserId(),
    //        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future;
    future = writeAccountLog(holder, 0);
    return future;
  }

  private Future<TransferMoneyHolder> writeAccountLogReceiver(TransferMoneyHolder holder) {
    //    log.info(
    //        "transaction {}-{}, step: write account log of receiver",
    //        holder.getSender().getUserId(),
    //        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future;
    future = writeAccountLog(holder, 1);
    return future;
  }

  public Future<TransferMoneyHolder> writeAccountLog(TransferMoneyHolder holder, int transferType) {
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    AccountLog accountLog = buildAccountLogModel(holder, transferType);
    transaction
        .execute(fintechDA.insertAccountLog(accountLog))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail(asyncResult.cause());
                log.error(
                    "write account_log failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  private AccountLog buildAccountLogModel(TransferMoneyHolder holder, int transferType) {
    return AccountLog.builder()
        .id(GenerationUtils.generateId())
        .transferType(transferType)
        .userId(
            transferType == 0 ? holder.getSender().getUserId() : holder.getReceiver().getUserId())
        .transferId(holder.getTransferId())
        .balance(
            transferType == 0 ? holder.getSender().getBalance() : holder.getReceiver().getBalance())
        .recordedTime(holder.getRecordedTime())
        .build();
  }

  private Future<TransferMoneyHolder> logTransferMessage(TransferMoneyHolder holder) {
    //    log.info(
    //        "insert to messages as transfer message, {}-{}",
    //        holder.getSender().getUserId(),
    //        holder.getRequest().getReceiverId());
    Future<TransferMoneyHolder> future = Future.future();
    WsMessage wsMessage =
        WsMessage.builder()
            .id(GenerationUtils.generateId())
            .senderId(holder.getSender().getUserId())
            .receiverId(holder.getRequest().getReceiverId())
            .message(String.valueOf(holder.getRequest().getAmount()))
            .messageType(1)
            .createTime(holder.getRecordedTime())
            .build();
    TransactionImpl transaction = (TransactionImpl) holder.getTransaction();
    holder.setConnection(transaction.getConnection().unwrap());
    chatDA
        .insertWithConnParam(wsMessage, holder.getConnection())
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                chatCache.addToList(wsMessage);
                future.complete(holder);
              } else {
                future.fail(asyncResult.cause());
                log.error(
                    "log transfer message failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  //  Support steps for real time chat and cache
  private Future<TransferMoneyHolder> appendToTransactionHistoryCache(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    fintechCache
        .appendToTransactionHistory(
            buildHistoryItemCache(holder, 0), holder.getSender().getUserId())
        .compose(
            next ->
                fintechCache.appendToTransactionHistory(
                    buildHistoryItemCache(holder, 1), holder.getRequest().getReceiverId()))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                log.error(
                    "append transaction history to cache failed, cause=", asyncResult.cause());
                future.fail(asyncResult.cause());
              }
            });
    return future;
  }

  private HistoryItem buildHistoryItemCache(TransferMoneyHolder holder, int i) {
    return HistoryItem.builder()
        .senderId(holder.getSender().getUserId())
        .receiverId(holder.getRequest().getReceiverId())
        .amount(holder.getRequest().getAmount())
        .description(holder.getRequest().getDescription())
        .recordedTime(holder.getRecordedTime())
        .transferType(i)
        .build();
  }

  private void notifyTransferMoney(TransferMoneyHolder holder) {
    TransferMoneyResponse.Data data =
        TransferMoneyResponse.Data.newBuilder()
            .setBalance(holder.getReceiver().getBalance())
            .setLastUpdated(holder.getRecordedTime())
            .setTransaction(
                TransactionHistory.newBuilder()
                    .setUserId(holder.getSender().getUserId())
                    .setAmount(holder.getRequest().getAmount())
                    .setDescription(holder.getRequest().getDescription())
                    .setTransferType(TransactionHistory.TransferType.RECEIVE)
                    .build())
            .build();
    WsMessage transferMoneyMessage =
        WsMessage.builder()
            .type("TRANSFER_MONEY")
            .receiverId(holder.getRequest().getReceiverId())
            .transferMoneyData(data)
            .build();
    wsHandler.notifyTransferMoney(transferMoneyMessage);
  }

  private Future<TransferMoneyHolder> updateLastMessageSender(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    String lastMessageSender =
        String.format(
            "Bạn: [Chuyển tiền] cho %s %d VND",
            holder.getReceiver().getName(), holder.getRequest().getAmount());
    userDA
        .updateLastMessage(
            lastMessageSender,
            holder.getSender().getUserId(),
            holder.getRequest().getReceiverId(),
            holder.getConnection())
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                log.error("update last message failed, cause=", asyncResult.cause());
                future.fail(asyncResult.cause());
              }
            });
    return future;
  }

  public Future<Void> updateLastMessagesAndUnseenMessagesReceiver(TransferMoneyHolder holder) {
    Future<Void> future = Future.future();
    String lastMessageReceiver =
        String.format(
            "[Nhận tiền] từ %s %d VND",
            holder.getSender().getName(), holder.getRequest().getAmount());
    userDA
        .updateLastMessageAndUnseenMessages(
            lastMessageReceiver,
            holder.getReceiver().getUserId(),
            holder.getSender().getUserId(),
            holder.getConnection())
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete();
              } else {
                log.error("increase unseen messages receiver failed, cause=", asyncResult.cause());
                future.fail(asyncResult.cause());
              }
            });
    return future;
  }
}
