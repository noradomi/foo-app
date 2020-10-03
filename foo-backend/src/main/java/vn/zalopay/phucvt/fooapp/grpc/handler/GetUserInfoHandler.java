package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.User;

@Log4j2
@Builder
public class GetUserInfoHandler {
  private final UserDA userDA;

  public void handle(
      GetUserInfoRequest request, StreamObserver<GetUserInfoResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call markRead from userId={}", userId);
    String retrivedUserId = request.getUserId();
    userDA
        .selectUserById(retrivedUserId)
        .setHandler(
            ayncResult -> {
              GetUserInfoResponse response;
              if (ayncResult.succeeded()) {
                User u = ayncResult.result();
                UserInfo user = UserInfo.newBuilder().setUserId(u.getUserId()).setName(u.getName()).build();
                GetUserInfoResponse.Data data =
                    GetUserInfoResponse.Data.newBuilder().setUser(user).build();
                response =
                    GetUserInfoResponse.newBuilder()
                        .setData(data)
                        .setStatus(Status.newBuilder().setCode(Code.OK).build())
                        .build();
              } else {
                response =
                    GetUserInfoResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
              }
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }
}
