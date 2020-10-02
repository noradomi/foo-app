package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.exceptions.TransferMoneyException;
import vn.zalopay.phucvt.fooapp.model.AccountLog;
import vn.zalopay.phucvt.fooapp.model.Transfer;
import vn.zalopay.phucvt.fooapp.model.TransferMoneyHolder;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;

import java.time.Instant;

@Log4j2
@Builder
public class FintechServiceImpl extends FintechServiceGrpc.FintechServiceImplBase {
  private final UserDA userDA;
  private final FintechDA fintechDA;
  private final TransactionProvider transactionProvider;

  @Override
  public void getBalance(
      GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call getBalance from userId={}", userId);
    Future<User> userAuth = userDA.selectUserById(userId);
    userAuth.setHandler(
        userAsyncResult -> {
          if (userAsyncResult.succeeded()) {
            User user = userAsyncResult.result();
            long balance = user.getBalance();
            Status status = Status.newBuilder().setCode(Code.OK).build();
            GetBalanceResponse.Data data =
                GetBalanceResponse.Data.newBuilder()
                    .setBalance(balance)
                    .setLastUpdated(user.getLastUpdated())
                    .build();
            GetBalanceResponse getBalanceResponse =
                GetBalanceResponse.newBuilder().setData(data).setStatus(status).build();

            responseObserver.onNext(getBalanceResponse);
            responseObserver.onCompleted();
          }
        });
  }

  @Override
  public void transferMoney(
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
                TransferMoneyResponse response =
                    TransferMoneyResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.OK).build())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
              } else {
                transaction.rollback();
                log.error(
                    "transfer money transaction failed, cause={}",
                    ExceptionUtil.getDetail(holderAsyncResult.cause()));
                handleExceptionResponse(holderAsyncResult.cause(), responseObserver);
              }
            });
  }

  public void handleExceptionResponse(
      Throwable throwable, StreamObserver<TransferMoneyResponse> responseObserver) {
    Status status = null;
    if (throwable instanceof TransferMoneyException) {
      status = Status.newBuilder().setCode(((TransferMoneyException) throwable).getCode()).build();
    } else {
      status = Status.newBuilder().setCode(Code.INTERNAL).build();
    }
    TransferMoneyResponse response = TransferMoneyResponse.newBuilder().setStatus(status).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  public Future<TransferMoneyHolder> getUserAuth(String userId, TransferMoneyHolder holder) {
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

  public Future<TransferMoneyHolder> validatePassword(TransferMoneyHolder holder) {
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

  public Future<TransferMoneyHolder> checkBalance(TransferMoneyHolder holder) {
    log.info("check balance");
    Future<TransferMoneyHolder> future = Future.future();
    String userId = holder.getUserAuth().getUserId();
    userDA
        .selectUserById(userId)
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                User userAuth = userAsyncResult.result();
                holder.setUserAuth(userAuth);
                long balance = userAuth.getBalance();
                if (balance >= holder.getRequest().getAmount()) {
                  future.complete(holder);
                } else {
                  future.fail(
                      new TransferMoneyException(
                          "Balance less than transferred amount", Code.NOT_ENOUGH_MONEY));
                }
              } else {
                future.fail(userAsyncResult.cause());
                log.error(
                    "get user auth failed, cause={}",
                    ExceptionUtil.getDetail(userAsyncResult.cause()));
              }
            });

    return future;
  }

  public Future<TransferMoneyHolder> withdraw(TransferMoneyHolder holder) {
    log.info("withdraw userId={}", holder.getUserAuth().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String userId = holder.getUserAuth().getUserId();
    long amount = holder.getRequest().getAmount();
    amount = -amount;
    transaction
        .execute(fintechDA.updateBalance(userId, amount, Instant.now().getEpochSecond()))
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

  public Future<TransferMoneyHolder> deposit(TransferMoneyHolder holder) {
    log.info(
        "deposit userId={} with amount={}",
        holder.getRequest().getReceiver(),
        holder.getRequest().getAmount());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getRequest().getReceiver();
    long amount = holder.getRequest().getAmount();
    transaction
        .execute(fintechDA.updateBalance(receiverId, amount, Instant.now().getEpochSecond()))
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

  public Future<TransferMoneyHolder> logTransfer(TransferMoneyHolder holder) {
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
            .recordedTime(Instant.now().getEpochSecond()) // now
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

  public Future<TransferMoneyHolder> logSenderAccountLog(TransferMoneyHolder holder) {
    log.info("log account_log userId={}", holder.getUserAuth().getUserId());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    logAccountLog(holder, future, transaction, holder.getUserAuth().getUserId(), 0);
    return future;
  }

  public Future<TransferMoneyHolder> logReceiverAccountLog(TransferMoneyHolder holder) {
    log.info("log account_log userId={}", holder.getRequest().getReceiver());
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    logAccountLog(holder, future, transaction, holder.getRequest().getReceiver(), 1);
    return future;
  }

  private void logAccountLog(
      TransferMoneyHolder holder,
      Future<TransferMoneyHolder> future,
      Transaction transaction,
      String userId,
      int transferType) {
    userDA
        .selectUserById(userId)
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                User user = userAsyncResult.result();
                AccountLog accountLog =
                    AccountLog.builder()
                        .id(GenerationUtils.generateId())
                        .transferType(transferType)
                        .userId(userId)
                        .transferId(holder.getTransferId())
                        .balance(user.getBalance()) // ?
                        .recordedTime(Instant.now().getEpochSecond())
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
              } else {
                future.fail("Get user auth failed");
                log.error(
                    "get user auth failed, cause={}",
                    ExceptionUtil.getDetail(userAsyncResult.cause()));
              }
            });
  }
}
