package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.model.User;

@Log4j2
@Builder
public class FintechServiceImpl extends FintechServiceGrpc.FintechServiceImplBase {
  private final UserDA userDA;

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
            Long balance = user.getBalance();
            Status status = Status.newBuilder().setCode(Code.OK).build();
            GetBalanceResponse.Data data =
                GetBalanceResponse.Data.newBuilder()
                    .setBalance(balance)
                    .setLastUpdate(user.getLastUpdated())
                    .build();
            GetBalanceResponse getBalanceResponse =
                GetBalanceResponse.newBuilder().setData(data).setStatus(status).build();

            responseObserver.onNext(getBalanceResponse);
            responseObserver.onCompleted();
          }
        });
  }
}
