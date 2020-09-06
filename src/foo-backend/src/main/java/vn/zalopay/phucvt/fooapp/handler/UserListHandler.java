package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.data.UsersDataResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.JwtUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Log4j2
public class UserListHandler extends BaseHandler {
  private final UserDA userDA;
  private final UserCache userCache;
  private final JwtUtils jwtUtils;
  private final WSHandler wsHandler;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    Future<BaseResponse> future = Future.future();
    String userId = baseRequest.getPrincipal().getString("userId");

    userCache
        .getUserList()
        .setHandler(
            listAsyncResult -> {
              if (listAsyncResult.succeeded()) {
                List<User> userList = listAsyncResult.result();
                if (userList.size() > 0) {
                  getUserListFromCache(future, userList, userId);
                } else {
                  getUserListFromDB(future, userId);
                }
              } else {
                getUserListFromDB(future, userId);
              }
            });
    return future;
  }

  private void getUserListFromCache(
      Future<BaseResponse> future, List<User> userList, String userId) {
    userList =
        userList.stream().filter(u -> !u.getUserId().equals(userId)).collect(Collectors.toList());
    log.info("cache hit, user list size={}", userList.size());
    setOnlineStatus(userList);
    UsersDataResponse usersDR = UsersDataResponse.builder().items(userList).build();
    BaseResponse baseResponse =
        BaseResponse.builder().statusCode(HttpResponseStatus.OK.code()).data(usersDR).build();
    future.complete(baseResponse);
  }

  private void setOnlineStatus(List<User> userList) {
    Set<String> onlineUserIds = wsHandler.getOnlineUserIds();
    log.debug("get onlien user ids, set={}", onlineUserIds);
    if (onlineUserIds != null) {
      userList.forEach(user -> user.setOnline(onlineUserIds.contains(user.getUserId())));
    }
  }

  private void getUserListFromDB(Future<BaseResponse> future, String userId) {
    userDA
        .selectListUser()
        .setHandler(
            listAsyncResult -> {
              if (listAsyncResult.succeeded()) {
                userCache.setUserList(listAsyncResult.result());
                List<User> userList =
                    listAsyncResult.result().stream()
                        .filter(u -> !u.getUserId().equals(userId))
                        .collect(Collectors.toList());
                log.trace("cache miss, get user list from db, size={}", userList.size());

                //                setOnlineStatus(userList);
                UsersDataResponse usersDR = UsersDataResponse.builder().items(userList).build();
                BaseResponse baseResponse =
                    BaseResponse.builder()
                        .statusCode(HttpResponseStatus.OK.code())
                        .data(usersDR)
                        .build();
                future.complete(baseResponse);
              }
            });
  }
}
