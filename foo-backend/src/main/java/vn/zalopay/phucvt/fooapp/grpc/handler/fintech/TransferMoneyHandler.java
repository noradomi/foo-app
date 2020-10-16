package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.grpc.stub.StreamObserver;
import io.vertx.core.CompositeFuture;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Log4j2
public class TransferMoneyHandler {
  private final UserDA userDA;
  private final FintechDA fintechDA;
  private final FintechCache fintechCache;
  private final ChatDA chatDA;
  private final ChatCache chatCache;
  private final WSHandler wsHandler;
  private final TransactionProvider transactionProvider;

  public void handle(
      TransferMoneyRequest request, StreamObserver<TransferMoneyResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info(
        "gRPC call: transferMoney from {} to {} with amount={}",
        userId,
        request.getReceiverId(),
        request.getAmount());
    //    Init holder
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    getUserAuth(userId, holder)
        .compose(this::validateReceiverId)
        .compose(this::validatePassword)
        .compose(this::validateAmountTransfer)
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                TransferMoneyHolder validatedHolder = asyncResult.result();
                log.info("validated password, now start transaction");
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
    transaction
        .begin()
        .compose(next -> checkBalance(validatedHolder))
        .compose(this::debit)
        .compose(this::credit)
        .compose(this::writeTransferLog)
        .compose(this::writeAccountLogSender)
        .compose(this::writeAccountLogReceiver)
        .compose(this::logTransferMessage)
        .setHandler(
            holderAsyncResult -> {
              if (holderAsyncResult.succeeded()) {
                log.debug("Transaction successfully");
                transaction.commit();
                TransferMoneyHolder holder = holderAsyncResult.result();
                TransferMoneyResponse response = buildSuccessTransferMoneyResponse(holder);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                notifyTransferMoney(holder);
                appendToTransactionHistoryCache(holder);
                userDA.updateLastMessage(
                    "Bạn: [Chuyển tiền] cho "
                        + holder.getReceiver().getName()
                        + " "
                        + holder.getRequest().getAmount()
                        + " VND",
                    holder.getSender().getUserId(),
                    holder.getRequest().getReceiverId());
                userDA.updateLastMessage(
                    "[Nhận tiền] từ "
                        + holder.getSender().getName()
                        + " "
                        + holder.getRequest().getAmount()
                        + " VND",
                    holder.getRequest().getReceiverId(),
                    holder.getSender().getUserId());
                userDA.increaseUnseenMessages(
                    holder.getRequest().getReceiverId(), holder.getSender().getUserId());
              } else {
                transaction.rollback();
                log.error(
                    "transfer money transaction failed, cause={}",
                    ExceptionUtil.getDetail(holderAsyncResult.cause()));
                handleExceptionResponse(holderAsyncResult.cause(), responseObserver);
              }
            });
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

  private Future<TransferMoneyHolder> getUserAuth(String userId, TransferMoneyHolder holder) {
    log.info("get user auth for userId={}", userId);
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
                future.fail("Get user auth failed");
                log.error(
                    "get user auth failed, cause={}",
                    ExceptionUtil.getDetail(userAsyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> validateReceiverId(TransferMoneyHolder holder) {
    log.info("validate receiver id");
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
                          "Receiver Id not found", Code.USER_ID_NOT_FOUND)); // note
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
    log.info("validate amount transfer");
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
    log.info("validate password");
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

  public Future<TransferMoneyHolder> checkBalance(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    String userId = holder.getSender().getUserId();
    log.info(
        "transaction {}-{} , step : check balance", userId, holder.getRequest().getReceiverId());
    List<Future> selectedUserBalanceList = new ArrayList<>();
    selectedUserBalanceList.add(fintechDA.selectUserForUpdate(userId));
    selectedUserBalanceList.add(fintechDA.selectUserForUpdate(holder.getRequest().getReceiverId()));
    CompositeFuture compositeFuture = CompositeFuture.all(selectedUserBalanceList);
    compositeFuture.setHandler(
        cpFutureAsyncResult -> {
          if (cpFutureAsyncResult.succeeded()) {
            User sender = compositeFuture.resultAt(0);
            User receiver = compositeFuture.resultAt(1);
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
            future.fail(cpFutureAsyncResult.cause());
            log.error(
                "get user balance failed, cause={}",
                ExceptionUtil.getDetail(cpFutureAsyncResult.cause()));
          }
        });
    return future;
  }

  public Future<TransferMoneyHolder> debit(TransferMoneyHolder holder) {
    log.info(
        "transaction {}-{}, step: debit sender's balance",
        holder.getSender().getUserId(),
        holder.getReceiver().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String userId = holder.getSender().getUserId();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getSender().getBalance() - amount;
    transaction
        .execute(fintechDA.updateBalance(userId, newBalance, holder.getRecordedTime()))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                holder.getSender().setBalance(newBalance);
                holder.setRecordedTime(Instant.now().getEpochSecond());
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
    log.info(
        "deposit userId={} with amount={}",
        holder.getRequest().getReceiverId(),
        holder.getRequest().getAmount());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getRequest().getReceiverId();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getReceiver().getBalance() + amount;
    holder.getReceiver().setBalance(newBalance);
    holder.setRecordedTime(Instant.now().getEpochSecond());
    transaction
        .execute(fintechDA.updateBalance(receiverId, newBalance, holder.getRecordedTime()))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail("Deposit userId=" + receiverId + " failed");
                log.error("deposit failed, cause={}", ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> writeTransferLog(TransferMoneyHolder holder) {
    log.info(
        "log transfer {}={}", holder.getSender().getUserId(), holder.getRequest().getReceiverId());
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
    log.info("log account_log userId={}", holder.getSender().getUserId());
    Future<TransferMoneyHolder> future;
    future = writeAccountLog(holder, 0);
    return future;
  }

  private Future<TransferMoneyHolder> writeAccountLogReceiver(TransferMoneyHolder holder) {
    log.info("log account_log userId={}", holder.getRequest().getReceiverId());
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
            transferType == 0
                ? holder.getSender().getUserId()
                : holder.getReceiver().getUserId())
        .transferId(holder.getTransferId())
        .balance(
            transferType == 0
                ? holder.getSender().getBalance()
                : holder.getReceiver().getBalance())
        .recordedTime(holder.getRecordedTime())
        .build();
  }

  private Future<TransferMoneyHolder> logTransferMessage(TransferMoneyHolder holder) {
    log.info(
        "insert to messages as transfer message, {}-{}",
        holder.getSender().getUserId(),
        holder.getRequest().getReceiverId());
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
    chatDA
        .insert(wsMessage)
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

  private void appendToTransactionHistoryCache(TransferMoneyHolder holder) {
    HistoryItem historySender =
        HistoryItem.builder()
            .senderId(holder.getSender().getUserId())
            .receiverId(holder.getRequest().getReceiverId())
            .amount(holder.getRequest().getAmount())
            .description(holder.getRequest().getDescription())
            .recordedTime(holder.getRecordedTime())
            .transferType(0)
            .build();

    HistoryItem historyReceiver =
        HistoryItem.builder()
            .senderId(holder.getSender().getUserId())
            .receiverId(holder.getRequest().getReceiverId())
            .amount(holder.getRequest().getAmount())
            .description(holder.getRequest().getDescription())
            .recordedTime(holder.getRecordedTime())
            .transferType(1)
            .build();

    fintechCache.appendToTransactionHistory(historySender, holder.getSender().getUserId());
    fintechCache.appendToTransactionHistory(historyReceiver, holder.getRequest().getReceiverId());
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
}
