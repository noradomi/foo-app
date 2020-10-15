package vn.zalopay.phucvt.fooapp.grpc.handler.chat;

import io.grpc.stub.StreamObserver;
import io.vertx.core.CompositeFuture;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.Friend;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;

import java.util.Set;

@Log4j2
@Builder
public class AddFriendHandler {
  private final UserDA userDA;
  private final UserCache userCache;
  private final WSHandler wsHandler;

  public void handle(AddFriendRequest request, StreamObserver<AddFriendResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    String friendId = request.getUserId();
    log.info("gRPC call addFriend {}--{}", userId, friendId);
    Friend friendModelRequest =
        Friend.builder()
            .id(GenerationUtils.generateId())
            .userId(userId)
            .friendId(friendId)
            .unreadMessages(0)
            .lastMessage("")
            .build();
    Friend friendModelApprove =
        Friend.builder()
            .id(GenerationUtils.generateId())
            .userId(friendId)
            .friendId(userId)
            .unreadMessages(0)
            .lastMessage("")
            .build();
    userDA
        .addFriend(friendModelRequest)
        .compose(next -> userDA.addFriend(friendModelApprove))
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                CompositeFuture cp =
                    CompositeFuture.all(
                        userDA.selectUserById(userId), userDA.selectUserById(friendId));
                cp.setHandler(
                    ar -> {
                      AddFriendResponse response;
                      if (ar.succeeded()) {
                        UserInfo user = mapToUserInfo(cp.resultAt(0));
                        UserInfo newFriend = mapToUserInfo(cp.resultAt(1));
                        userCache.appendFriendList(
                            UserFriendItem.builder()
                                .id(newFriend.getUserId())
                                .name(newFriend.getName())
                                .unreadMessages(newFriend.getUnreadMessages())
                                .lastMessage(newFriend.getLastMessage())
                                .build(),
                            userId);
                        response = buildSuccessResponse(userId, friendId, user, newFriend);
                      } else {
                        log.error(
                            "insert to friend table failed, cause={}",
                            ExceptionUtil.getDetail(asyncResult.cause()));
                        Status status = Status.newBuilder().setCode(Code.INTERNAL).build();
                        response = AddFriendResponse.newBuilder().setStatus(status).build();
                      }
                      responseObserver.onNext(response);
                      responseObserver.onCompleted();
                    });
              }
            });
  }

  private AddFriendResponse buildSuccessResponse(
      String userId, String friendId, UserInfo user, UserInfo newFriend) {
    WsMessage wsMessage =
        WsMessage.builder()
            .type("ADD_FRIEND")
            .senderId(userId)
            .receiverId(friendId)
            .userInfo(user)
            .build();
    wsHandler.notifyAddFriend(wsMessage);
    Status status = Status.newBuilder().setCode(Code.OK).build();
    return AddFriendResponse.newBuilder()
        .setData(AddFriendResponse.Data.newBuilder().setUser(newFriend).build())
        .setStatus(status)
        .build();
  }

  private boolean getOnlineStatus(String userId) {
    Set<String> onlineUserIds = wsHandler.getOnlineUserIds();
    if (onlineUserIds != null) {
      return onlineUserIds.contains(userId);
    }
    return false;
  }

  private UserInfo mapToUserInfo(User u) {
    return UserInfo.newBuilder()
        .setUserId(u.getUserId())
        .setName(u.getName())
        .setUnreadMessages(0)
        .setLastMessage("")
        .setIsOnline(getOnlineStatus(u.getUserId()))
        .build();
  }
}
