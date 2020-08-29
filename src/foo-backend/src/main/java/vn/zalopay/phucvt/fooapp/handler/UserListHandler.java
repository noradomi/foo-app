package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserListItem;
import vn.zalopay.phucvt.fooapp.model.UserListResponse;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

@Builder
@Log4j2
public class UserListHandler extends BaseHandler {

  private final UserDA userDA;

  private final JWTUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {

    Future<BaseResponse> future = Future.future();

    Future<String> userIdFuture = jwtUtils.getUserIdFromToken(baseRequest);
    userIdFuture.compose(
        id -> {
          userDA
              .selectListUsersById(id)
              .compose(
                  users -> {
                    UserListResponse userListResponse = new UserListResponse();
                    log.info("got data with {}", users.size());
                    for (User u : users) {
                      UserListItem item =
                          UserListItem.builder()
                              .userId(u.getUserId())
                              .username(u.getFullname())
                              .build();
                      //                      Future<String> conversation_id =
                      //                          userDA.getSingleConversationId(id, u.getUserId());
                      //                      conversation_id.compose(
                      //                          con_id -> {
                      //                            item.setConversationId(con_id);
                      //                            log.info(u.getUsername());
                      //                            userListResponse.getItems().add(item);
                      //                          },
                      //                          Future.future()
                      //                              .setHandler(
                      //                                  handler -> {
                      //                                    future.fail(handler.cause());
                      //                                  }));
                      userListResponse.getItems().add(item);
                    }
                    log.info("build done");
                    SuccessResponse successResponse =
                        SuccessResponse.builder().data(userListResponse).build();
                    successResponse.setStatus(200);
                    future.complete(successResponse);
                  },
                  Future.future()
                      .setHandler(
                          handler -> {
                            future.fail(handler.cause());
                          }));
        },
        Future.future()
            .setHandler(
                handler -> {
                  future.fail(handler.cause());
                }));

    return future;
  }
}
