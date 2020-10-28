package vn.zalopay.phucvt.fooapp.grpc.handler.chat;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Log4j2
public class GetFriendListHandler {
  private static final String METRIC = "GetFriendListHandler";
  private final UserDA userDA;
  private final UserCache userCache;
  private final WSHandler wsHandler;

  public void handle(
      GetFriendListRequest request, StreamObserver<GetFriendListResponse> responseObserver) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    String userId = AuthInterceptor.USER_ID.get();
    log.info("gRPC call getFriendList from userId={}", userId);
    getFriendListFromDB(userId, responseObserver, tracker);
  }

  private void getFriendListFromDB(
      String userId,
      StreamObserver<GetFriendListResponse> responseObserver,
      Tracker.TrackerBuilder tracker) {
    userDA
        .getFriendList(userId)
        .setHandler(
            listAsyncResult -> {
              GetFriendListResponse response;
              if (listAsyncResult.succeeded()) {
                List<UserFriendItem> friendList = listAsyncResult.result();
                response = handleSuccessResponse(friendList);
              } else {
                log.error("get friend list failed, cause=", listAsyncResult.cause());
                response =
                    GetFriendListResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
              }
              responseObserver.onNext(response);
              responseObserver.onCompleted();
              tracker.build().record();
            });
  }

  private GetFriendListResponse handleSuccessResponse(List<UserFriendItem> userList) {
    GetFriendListResponse.Data.Builder builder = GetFriendListResponse.Data.newBuilder();
    builder.addAllItems(mapToUserInfo(userList));
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
