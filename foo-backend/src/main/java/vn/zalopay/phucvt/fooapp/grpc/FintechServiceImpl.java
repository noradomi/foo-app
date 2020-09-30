package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.model.User;

@Log4j2
@Builder
public class FintechServiceImpl extends FintechServiceGrpc.FintechServiceImplBase {
  private final UserDA userDA;
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
    //    String userId = AuthInterceptor.USER_ID.get();
    //    //    Step 1: Validate password
    //    String password = request.getConfirmPassword();
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
    userDA.selectUserById(userId)
            .compose(next -> this::validatePassword)
            .setHandler()
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call transferMoney from userId={}", userId);
    System.out.println("Confirm password: "+request.getConfirmPassword());
    TransferMoneyResponse response =
        TransferMoneyResponse.newBuilder()
            .setStatus(Status.newBuilder().setCode(Code.OK).build())
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

    public Future<Void> validatePassword() {
      Future<Void> future = Future.future();
      if (BCrypt.checkpw(user.getPassword(), password)) {
        future.complete();
      } else future.fail("Password not match");
      return future;
    }

}
