package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Log4j2
public class GetFriendListHandler {
  private final UserDA userDA;
  private final UserCache userCache;
  private final WSHandler wsHandler;

  public void handle(
      GetFriendListRequest request, StreamObserver<GetFriendListResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call getFriendList from userId={}", userId);
    userCache
        .getFriendList(userId)
        .setHandler(
            cacheListAsyncResult -> {
              if (cacheListAsyncResult.succeeded()) {
                List<UserInfo> userList = cacheListAsyncResult.result();
                if (userList.size() > 0) {
                  log.info("get friend list from cache, cache hit");
                  GetFriendListResponse response = handleSuccessResponse(userList);
                  responseObserver.onNext(response);
                  responseObserver.onCompleted();
                } else {
                  getFriendListFromDB(userId, responseObserver);
                }
              } else {
                getFriendListFromDB(userId, responseObserver);
              }
            });
  }

  private void getFriendListFromDB(
      String userId, StreamObserver<GetFriendListResponse> responseObserver) {
    userDA
        .getFriendList(userId)
        .setHandler(
            listAsyncResult -> {
              GetFriendListResponse response;
              if (listAsyncResult.succeeded()) {
                List<UserFriendItem> friendList = listAsyncResult.result();
                List<UserInfo> userInfoList = mapToUserInfo(friendList);
                userCache.setFriendList(userInfoList, userId);
                response = handleSuccessResponse(userInfoList);
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

  private GetFriendListResponse handleSuccessResponse(List<UserInfo> userList) {
    GetFriendListResponse.Data.Builder builder = GetFriendListResponse.Data.newBuilder();
    builder.addAllItems(userList);
    GetFriendListResponse.Data data = builder.build();
    return GetFriendListResponse.newBuilder()
        .setData(data)
        .setStatus(Status.newBuilder().setCode(Code.OK).build())
        .build();
  }

  private boolean getOnlineStatus(UserFriendItem u) {
    Set<String> onlineUserIds = wsHandler.getOnlineUserIds();
    if (onlineUserIds != null) {
      return onlineUserIds.contains(u.getId());
    }
    return false;
  }

  public List<UserInfo> mapToUserInfo(List<UserFriendItem> userList) {
    List<UserInfo> res = new ArrayList<>();
    userList.forEach(
        u ->
            res.add(
                UserInfo.newBuilder()
                    .setUserId(u.getId())
                    .setName(u.getName())
                    .setUnreadMessages(u.getUnreadMessages())
                    .setLastMessage(u.getLastMessage())
                    .setIsOnline(getOnlineStatus(u))
                    .build()));
    return res;
  }
}
