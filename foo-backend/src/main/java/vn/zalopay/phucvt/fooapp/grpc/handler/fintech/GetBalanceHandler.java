package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.GetBalanceRequest;
import vn.zalopay.phucvt.fooapp.fintech.GetBalanceResponse;
import vn.zalopay.phucvt.fooapp.fintech.Status;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

@Builder
@Log4j2
public class GetBalanceHandler {
  private static final String METRIC = "GetBalanceHandler";
  private final UserDA userDA;

  public void handle(
      GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call getBalance from userId={}", userId);
    Future<User> userAuth = userDA.selectUserById(userId);
    userAuth.setHandler(
        userAsyncResult -> {
          if (userAsyncResult.succeeded()) {
            User user = userAsyncResult.result();
            GetBalanceResponse getBalanceResponse = buildResponse(user);
            responseObserver.onNext(getBalanceResponse);
          } else {
            Status status = Status.newBuilder().setCode(Code.INTERNAL).build();
            GetBalanceResponse response = GetBalanceResponse.newBuilder().setStatus(status).build();
            responseObserver.onNext(response);
          }
          responseObserver.onCompleted();
          tracker.step("handle").code("ok").build().record();
        });
  }

  public GetBalanceResponse buildResponse(User user) {
    long balance = user.getBalance();
    Status status = Status.newBuilder().setCode(Code.OK).build();
    GetBalanceResponse.Data data =
        GetBalanceResponse.Data.newBuilder()
            .setBalance(balance)
            .setLastUpdated(user.getLastUpdated())
            .build();
    return GetBalanceResponse.newBuilder().setData(data).setStatus(status).build();
  }
}
