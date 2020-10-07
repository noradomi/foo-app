package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
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
  private final WSHandler wsHandler;
  private final TransactionProvider transactionProvider;

  public void handle(
      TransferMoneyRequest request, StreamObserver<TransferMoneyResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info(
        "gRPC call transferMoney from userId={} to userId={} with amount={}",
        userId,
        request.getReceiver(),
        request.getAmount());
    //    Init holder
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    getUserAuth(userId, holder)
        .compose(this::validatePassword)
        .compose(this::validateReceiverId)
        .compose(this::validateAmountTransfer  )
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                TransferMoneyHolder validatedHolder = asyncResult.result();
                log.info("Validated pwd, now start transaction");
                transferMoneyTransaction(responseObserver, validatedHolder);
              } else {
                log.error(
                    "validate password failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
                handleExceptionResponse(asyncResult.cause(), responseObserver);
              }
            });
  }

  private void transferMoneyTransaction(
      StreamObserver<TransferMoneyResponse> responseObserver, TransferMoneyHolder validatedHolder) {
    Transaction transaction = transactionProvider.newTransaction();
    validatedHolder.setTransaction(transaction);
    transaction
        .begin()
        .compose(next -> checkBalance(validatedHolder))
        .compose(this::withdraw) // withdraw sender by amount
        .compose(this::deposit) // deposit receiver by amount
        .compose(this::logTransfer)
        .compose(this::logSenderAccountLog)
        .compose(this::logReceiverAccountLog)
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
            .setBalance(holder.getSenderBalance())
            .setLastUpdated(holder.getRecordedTime())
            .setTransaction(
                TransactionHistory.newBuilder()
                    .setUserId(holder.getRequest().getReceiver())
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

  private void notifyTransferMoney(TransferMoneyHolder holder) {
    TransferMoneyResponse.Data data =
        TransferMoneyResponse.Data.newBuilder()
            .setBalance(holder.getReceiverBalance())
            .setLastUpdated(holder.getRecordedTime())
            .setTransaction(
                TransactionHistory.newBuilder()
                    .setUserId(holder.getUserAuth().getUserId())
                    .setAmount(holder.getRequest().getAmount())
                    .setDescription(holder.getRequest().getDescription())
                    .setTransferType(TransactionHistory.TransferType.RECEIVE)
                    .build())
            .build();
    WsMessage transferMoneyMessage =
        WsMessage.builder()
            .type("TRANSFER_MONEY")
            .receiverId(holder.getRequest().getReceiver())
            .transferMoneyData(data)
            .build();
    wsHandler.notifyTransferMoney(transferMoneyMessage);
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
                holder.setUserAuth(userAuth);
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

  private Future<TransferMoneyHolder> validateReceiverId(TransferMoneyHolder holder) {
    log.info("validate receiver id");
    Future<TransferMoneyHolder> future = Future.future();
    userDA
        .selectUserById(holder.getRequest().getReceiver())
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                User u = userAsyncResult.result();
                if (u == null) {
                  future.fail(
                      new TransferMoneyException(
                          "Receiver Id not found", Code.USER_ID_NOT_FOUND)); // note
                } else {
                  future.complete();
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

  private Future<TransferMoneyHolder> validateAmountTransfer(TransferMoneyHolder holder) {
    log.info("validate amount transfer");
    Future<TransferMoneyHolder> future = Future.future();
    long amount = holder.getRequest().getAmount();
    if (amount < 1000 || amount > 20000000 || amount % 1000 != 0) {
      future.fail(new TransferMoneyException("Amount transfer invalid", Code.INVALID_INPUT_MONEY));
    } else {
      future.complete();
    }
    return future;
  }

  private Future<TransferMoneyHolder> validatePassword(TransferMoneyHolder holder) {
    log.info("validate password");
    Future<TransferMoneyHolder> future = Future.future();
    User userAuth = holder.getUserAuth();
    String confirmPassword = holder.getRequest().getConfirmPassword();
    if (BCrypt.checkpw(confirmPassword, userAuth.getPassword())) {
      future.complete(holder);
    } else {
      future.fail(new TransferMoneyException("Confirm password not match", Code.INVALID_PASSWORD));
    }
    return future;
  }

  private Future<TransferMoneyHolder> checkBalance(TransferMoneyHolder holder) {
    log.info("check balance");
    Future<TransferMoneyHolder> future = Future.future();
    String userId = holder.getUserAuth().getUserId();
    List<Future> seleceUserBalanceList = new ArrayList<>();
    seleceUserBalanceList.add(fintechDA.selectUserForUpdate(userId));
    seleceUserBalanceList.add(fintechDA.selectUserForUpdate(holder.getRequest().getReceiver()));
    CompositeFuture cp = CompositeFuture.all(seleceUserBalanceList);
    cp.setHandler(
        cpFutureAsyncResult -> {
          if (cpFutureAsyncResult.succeeded()) {
            User sender = cp.resultAt(0);
            User receiver = cp.resultAt(1);
            if (sender.getBalance() >= holder.getRequest().getAmount()) {
              holder.setUserAuth(sender);
              holder.setSenderBalance(sender.getBalance());
              holder.setReceiverBalance(receiver.getBalance());
              future.complete(holder);
            } else {
              future.fail(
                  new TransferMoneyException(
                      "Balance less than transferred amount", Code.NOT_ENOUGH_MONEY));
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

  private Future<TransferMoneyHolder> withdraw(TransferMoneyHolder holder) {
    log.info("withdraw userId={}", holder.getUserAuth().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String userId = holder.getUserAuth().getUserId();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getSenderBalance() - amount;
    holder.setSenderBalance(newBalance);
    holder.setRecordedTime(Instant.now().getEpochSecond());
    transaction
        .execute(fintechDA.updateBalance(userId, newBalance, holder.getRecordedTime()))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail("Withdraw userId=" + userId + " failed");
                log.error(
                    "withdraw failed, cause={}", ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  private Future<TransferMoneyHolder> deposit(TransferMoneyHolder holder) {
    log.info(
        "deposit userId={} with amount={}",
        holder.getRequest().getReceiver(),
        holder.getRequest().getAmount());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getRequest().getReceiver();
    long amount = holder.getRequest().getAmount();
    long newBalance = holder.getReceiverBalance() + amount;
    holder.setReceiverBalance(newBalance);
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

  private Future<TransferMoneyHolder> logTransfer(TransferMoneyHolder holder) {
    log.info(
        "log transfer {}={}", holder.getUserAuth().getUserId(), holder.getRequest().getReceiver());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    Transfer transfer =
        Transfer.builder()
            .transferId(GenerationUtils.generateId())
            .amount(holder.getRequest().getAmount())
            .sender(holder.getUserAuth().getUserId())
            .receiver(holder.getRequest().getReceiver())
            .description(holder.getRequest().getDescription())
            .recordedTime(holder.getRecordedTime()) // now
            .build();
    transaction
        .execute(fintechDA.insertTransferLog(transfer))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                holder.setTransferId(transfer.getTransferId());
                future.complete(holder);
              } else {
                future.fail("Log transfer failed");
                log.error(
                    "log transfer failed, cause={}", ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
    return future;
  }

  private Future<TransferMoneyHolder> logSenderAccountLog(TransferMoneyHolder holder) {
    log.info("log account_log userId={}", holder.getUserAuth().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    logAccountLog(
        holder,
        future,
        transaction,
        holder.getUserAuth().getUserId(),
        holder.getSenderBalance(),
        0);
    return future;
  }

  private Future<TransferMoneyHolder> logReceiverAccountLog(TransferMoneyHolder holder) {
    log.info("log account_log userId={}", holder.getRequest().getReceiver());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    logAccountLog(
        holder,
        future,
        transaction,
        holder.getRequest().getReceiver(),
        holder.getReceiverBalance(),
        1);
    return future;
  }

  private void logAccountLog(
      TransferMoneyHolder holder,
      Future<TransferMoneyHolder> future,
      Transaction transaction,
      String userId,
      long balance,
      int transferType) {
    AccountLog accountLog =
        AccountLog.builder()
            .id(GenerationUtils.generateId())
            .transferType(transferType)
            .userId(userId)
            .transferId(holder.getTransferId())
            .balance(balance)
            .recordedTime(holder.getRecordedTime())
            .build();
    transaction
        .execute(fintechDA.insertAccountLog(accountLog))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail("Log sender account log failed");
                log.error(
                    "log account_log failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
              }
            });
  }
}
