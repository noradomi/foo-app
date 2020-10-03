package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.MarkReadRequest;
import vn.zalopay.phucvt.fooapp.fintech.MarkReadResponse;
import vn.zalopay.phucvt.fooapp.fintech.Status;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

@Builder
@Log4j2
public class MarkReadHandler {
  private final UserDA userDA;

  public void handle(MarkReadRequest request, StreamObserver<MarkReadResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call markRead from userId={}", userId);
    String markedUserId = request.getUserId();
    userDA
        .markRead(userId, markedUserId)
        .setHandler(
            ayncResult -> {
              Status status;
              if (ayncResult.succeeded()) {
                status = Status.newBuilder().setCode(Code.OK).build();
              } else {
                log.error(
                    "mark read failed, cause={}", ExceptionUtil.getDetail(ayncResult.cause()));
                status = Status.newBuilder().setCode(Code.INTERNAL).build();
              }
              MarkReadResponse response = MarkReadResponse.newBuilder().setStatus(status).build();
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }
}
