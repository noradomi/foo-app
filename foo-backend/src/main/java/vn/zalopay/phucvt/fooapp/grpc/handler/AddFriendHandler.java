package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.AddFriendRequest;
import vn.zalopay.phucvt.fooapp.fintech.AddFriendResponse;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.Status;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.Friend;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;

@Log4j2
@Builder
public class AddFriendHandler {
  private final UserDA userDA;

  public void handle(AddFriendRequest request, StreamObserver<AddFriendResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    String friendId = request.getUserId();
    log.info("gRPC call addFriend {}--{}", userId, friendId);
    Friend friendModel =
        Friend.builder()
            .id(GenerationUtils.generateId())
            .userId(userId)
            .friendId(friendId)
            .unreadMessages(0)
            .lastMessage("")
            .build();
    userDA
        .addFriend(friendModel)
        .setHandler(
            asyncResult -> {
              Status status;
              if (asyncResult.succeeded()) {
                status = Status.newBuilder().setCode(Code.OK).build();
              } else {
                log.error(
                    "insert to friend table failed, cause={}",
                    ExceptionUtil.getDetail(asyncResult.cause()));
                status = Status.newBuilder().setCode(Code.INTERNAL).build();
              }
              AddFriendResponse response = AddFriendResponse.newBuilder().setStatus(status).build();
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }
}
