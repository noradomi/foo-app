package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.UserListItem;
import vn.zalopay.phucvt.fooapp.model.UserListResponse;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

@Builder
public class UserListHandler extends BaseHandler {

  private final UserDA userDA;

  private final JWTUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {

    Future<BaseResponse> future = Future.future();

    Future<String> userIdFuture = jwtUtils.getUserIdFromToken(baseRequest);
    userIdFuture
            .compose(id -> userDA.selectListUsersById(id))
            .compose(res -> {
                UserListResponse userListResponse = new UserListResponse();
                res.forEach(v -> {
                    UserListItem item = UserListItem
                            .builder()
                            .userId(v.getUserId())
                            .username(v.getUsername())
                            .build();
                    userListResponse.getItems().add(item);
                });
                SuccessResponse successResponse = SuccessResponse.builder()
                        .data(userListResponse)
                        .build();
                successResponse.setStatus(200);
                future.complete(successResponse);
            },Future.future().setHandler(handler -> {
                future.fail(handler.cause());
            }));

    return future;
  }
}
