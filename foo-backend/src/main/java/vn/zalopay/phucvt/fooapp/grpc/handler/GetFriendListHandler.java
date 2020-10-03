package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;

import java.util.List;

@Builder
@Log4j2
public class GetFriendListHandler {
  private final UserDA userDA;

  public void handle(
      GetFriendListRequest request, StreamObserver<GetFriendListResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call getFriendList from userId={}", userId);
    userDA
        .getFriendList(userId)
        .setHandler(
            listAsyncResult -> {
              GetFriendListResponse response;
              if (listAsyncResult.succeeded()) {
                List<UserFriendItem> userList = listAsyncResult.result();
                userList.forEach(x -> System.out.println(x.getName()));
                response = handleSuccessResponse(userList);
              } else {
                log.error("get friend list failed, cause=", listAsyncResult.cause());
                response =
                    GetFriendListResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
              }
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }

  private GetFriendListResponse handleSuccessResponse(List<UserFriendItem> userList) {
    GetFriendListResponse.Data.Builder builder = GetFriendListResponse.Data.newBuilder();
    userList.forEach(x -> builder.addItems(mapToUserInfo(x)));
    GetFriendListResponse.Data data = builder.build();
    return GetFriendListResponse.newBuilder()
        .setData(data)
        .setStatus(Status.newBuilder().setCode(Code.OK).build())
        .build();
  }

  public UserInfo mapToUserInfo(UserFriendItem u) {
    return UserInfo.newBuilder()
        .setUserId(u.getId())
        .setName(u.getName())
        .setUnreadMessages(u.getUnreadMessages())
        .setLastMessage(u.getLastMessage())
        .setIsOnline(false)
        .build();
  }
}
