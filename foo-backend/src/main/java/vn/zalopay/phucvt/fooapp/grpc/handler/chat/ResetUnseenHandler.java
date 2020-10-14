package vn.zalopay.phucvt.fooapp.grpc.handler.chat;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.ResetUnseenRequest;
import vn.zalopay.phucvt.fooapp.fintech.ResetUnseenResponse;
import vn.zalopay.phucvt.fooapp.fintech.Status;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

@Builder
@Log4j2
public class ResetUnseenHandler {
  private final UserDA userDA;

  public void handle(
      ResetUnseenRequest request, StreamObserver<ResetUnseenResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call resetUnseen from userId={}", userId);
    String friendId = request.getUserId();
    userDA
        .resetUnseen(userId, friendId)
        .setHandler(
            asyncResult -> {
              Status status;
              if (asyncResult.succeeded()) {
                status = Status.newBuilder().setCode(Code.OK).build();
              } else {
                log.error(
                    "reset unseen failed, cause={}", ExceptionUtil.getDetail(asyncResult.cause()));
                status = Status.newBuilder().setCode(Code.INTERNAL).build();
              }
              ResetUnseenResponse response =
                  ResetUnseenResponse.newBuilder().setStatus(status).build();
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }
}
