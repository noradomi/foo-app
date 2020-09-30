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
import vn.zalopay.phucvt.fooapp.model.TransferMoneyHolder;
import vn.zalopay.phucvt.fooapp.model.User;

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
    String receiver = request.getReceiver();
    String password = request.getConfirmPassword();
    Long amount = request.getAmount();
    String description = request.getDescription();

    //    Future<User> userAuth = userDA.selectUserById(userId);
    //    userAuth.setHandler(
    //        userAsyncResult -> {
    //          if (userAsyncResult.succeeded()) {
    //            User user = userAsyncResult.result();
    //            if (BCrypt.checkpw(user.getPassword(),password)) {
    //              //                  do something
    //              Transaction transaction = transactionProvider.newTransaction();
    ////              transaction.begin()
    ////                      .compose()
    //            } else {
    //              TransferMoneyResponse response =
    //                  TransferMoneyResponse.newBuilder()
    //                      .setStatus(Status.newBuilder().setCode(Code.INVALID_PASSWORD).build())
    //                      .build();
    //              responseObserver.onNext(response);
    //              responseObserver.onCompleted();
    //            }
    //          }
    //        });
    //    userDA.selectUserById(userId)
    //            .compose(next -> this::validatePassword)
    //            .setHandler();
    getUserAuth(userId)
        .compose(this::validatePassword)
        .setHandler(
            asynResult -> {
              if (asynResult.succeeded()) {
                log.debug("Validated pwd, now start transaction");
                Transaction transaction = transactionProvider.newTransaction();
                //                transaction
                //                        .begin()
                //                        .compose(next -> )
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

  public Future<TransferMoneyHolder> getUserAuth(String userId) {
    Future<TransferMoneyHolder> future = Future.future();
    TransferMoneyHolder holder = new TransferMoneyHolder();
    userDA
        .selectUserById(userId)
        .setHandler(
            userAsyncResult -> {
              if (userAsyncResult.succeeded()) {
                holder.setUserAuth(userAsyncResult.result());
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
    String confirmPassword = holder.getConfirmPassword();
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
                if (balance < holder.getAmount()) {
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
    Long amount = holder.getAmount();
    amount = -amount;
    transaction.execute(fintechDA.updateBalance(userId, amount))
      .setHandler(asyncResult -> {
        if(asyncResult.succeeded()){
          future.complete(holder);
        }
        else{
          future.fail("With draw failed");
        }
      });
    return future;
  }

  public Future<TransferMoneyHolder> deposit(TransferMoneyHolder holder) {
    Future<TransferMoneyHolder> future = Future.future();
    Transaction transaction = holder.getTransaction();
    String receiverId = holder.getReceiverId();
    Long amount = holder.getAmount();
    transaction.execute(fintechDA.updateBalance(receiverId, amount))
            .setHandler(asyncResult -> {
              if(asyncResult.succeeded()){
                future.complete(holder);
              }
              else{
                future.fail("With draw failed");
              }
            });
    return future;
  }

  //    public Future<Void> validatePassword() {
  //      Future<Void> future = Future.future();
  //      if (BCrypt.checkpw(user.getPassword(), password)) {
  //        future.complete();
  //      } else future.fail("Password not match");
  //      return future;
  //    }

}
