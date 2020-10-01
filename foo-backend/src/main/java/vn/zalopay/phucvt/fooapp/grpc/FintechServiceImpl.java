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
import vn.zalopay.phucvt.fooapp.model.Transfer;
import vn.zalopay.phucvt.fooapp.model.TransferMoneyHolder;
import vn.zalopay.phucvt.fooapp.model.User;

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
    log.info("gRPC call transferMoney from userId={}", userId);
    //    Init holder
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);

    getUserAuth(userId, holder)
        .compose(this::validatePassword)
        .setHandler(
            asynResult -> {
              if (asynResult.succeeded()) {
                TransferMoneyHolder validatedHolder = asynResult.result();
                log.debug("Validated pwd, now start transaction");
                Transaction transaction = transactionProvider.newTransaction();
                transaction
                    .begin()
                    .compose(next -> checkBalance(validatedHolder))
                    .compose(this::withdraw) // withdraw sender by amount
                    .compose(this::deposit) // deposit receiver by amount
                    .setHandler(
                        holderAsyncResult -> {
                          if (holderAsyncResult.succeeded()) {
                            log.debug("Transaction successfully");
                          } else {
                            log.debug("Transaction failed");
                          }
                        });
              }
            });
    System.out.println("Confirm password: " + request.getConfirmPassword());
    TransferMoneyResponse response =
        TransferMoneyResponse.newBuilder()
            .setStatus(Status.newBuilder().setCode(Code.OK).build())
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  public Future<TransferMoneyHolder> getUserAuth(String userId, TransferMoneyHolder holder) {
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
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> validatePassword(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    User userAuth = holder.getUserAuth();
    String confirmPassword = holder.getRequest().getConfirmPassword();
    if (BCrypt.checkpw(userAuth.getPassword(), confirmPassword)) {
      future.complete(holder);
    } else {
      future.fail("ConfirmPassword not match");
    }
    return future;
  }

  public Future<TransferMoneyHolder> checkBalance(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    String userId = holder.getUserAuth().getUserId();
    userDA
        .selectUserById(userId)
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                long balance = userAsyncResult.result().getBalance();
                if (balance < holder.getRequest().getAmount()) {
                  future.complete(holder);
                } else {
                  future.fail("Balance less than transferred amount");
                }
              } else {
                future.fail("Get user auth failed");
              }
            });

    return future;
  }

  public Future<TransferMoneyHolder> withdraw(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String userId = holder.getUserAuth().getUserId();
    long amount = holder.getRequest().getAmount();
    amount = -amount;
    transaction
        .execute(fintechDA.updateBalance(userId, amount))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail("With draw failed");
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> deposit(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getReceiverId();
    long amount = holder.getRequest().getAmount();
    transaction
        .execute(fintechDA.updateBalance(receiverId, amount))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                future.complete(holder);
              } else {
                future.fail("With draw failed");
              }
            });
    return future;
  }

  public Future<TransferMoneyHolder> logTransfer(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    Transfer transfer =
        Transfer.builder()
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
              }
            });
    return future;
  }
}
