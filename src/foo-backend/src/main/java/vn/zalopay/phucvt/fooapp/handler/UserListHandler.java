package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserListItem;
import vn.zalopay.phucvt.fooapp.model.UserListResponse;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
@Log4j2
public class UserListHandler extends BaseHandler {

  private final UserDA userDA;
  private final UserCache userCache;
  private final JWTUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    log.info("Fetch user list");
    Future<BaseResponse> future = Future.future();

    Future<String> userIdFuture = jwtUtils.getUserIdFromToken(baseRequest);

    userIdFuture.compose(
        id -> {
          Future<List<User>> usersFuture = userCache.getUserList();
          usersFuture.setHandler(
              res -> {
                Future<List<User>> allUsersFuture = Future.future();
                if (res.succeeded()) {
                  log.info("Load user list: CACHE HIT");
                  allUsersFuture.complete(res.result());
                } else {
                    log.info("Load user list: CACHE MISS");
                  userDA
                      .selectListUser()
                      .setHandler(
                          userList -> {
                            if (userList.succeeded()) {
                              userCache.setUserList(userList.result());
                              allUsersFuture.complete(userList.result());
                            }
                          });
                }
                allUsersFuture.compose(
                    users -> {
                      List<Future> getStatusUserFutures = new ArrayList<>();
                      UserListResponse userListResponse = new UserListResponse();

                      for (User u : users) {
                        if (!u.getUserId().equals(id)) {
                          UserListItem item =
                              UserListItem.builder()
                                  .userId(u.getUserId())
                                  .fullname(u.getFullname())
                                  .build();
                          userListResponse.getItems().add(item);
                          getStatusUserFutures.add(userCache.isOnlineStatus(u.getUserId()));
                        }
                      }

                      CompositeFuture cp = CompositeFuture.all(getStatusUserFutures);
                      cp.setHandler(
                          ar -> {
                            if (ar.succeeded()) {
                              for (int index = 0; index < getStatusUserFutures.size(); ++index) {
                                userListResponse
                                    .getItems()
                                    .get(index)
                                    .setOnline(cp.resultAt(index));
                              }

                              SuccessResponse successResponse =
                                  SuccessResponse.builder().data(userListResponse).build();
                              successResponse.setStatus(HttpResponseStatus.OK.code());
                              future.complete(successResponse);
                            }
                          });
                    },
                    Future.future()
                        .setHandler(
                            handler -> {
                              future.fail(handler.cause());
                            }));
              });
        },
        Future.future()
            .setHandler(
                handler -> {
                  log.error("Load user list: RETRIEVE USER ID FAILED");
                  future.fail(handler.cause());
                }));
    return future;
  }
}
