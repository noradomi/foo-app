package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.data.LoginDataResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import vn.zalopay.phucvt.fooapp.utils.JwtUtils;

@Builder
@Log4j2
public class LoginHandler extends BaseHandler {
  private final JwtUtils jwtUtils;
  private final UserCache userCache;
  private final UserDA userDA;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    final User user = JsonProtoUtils.parseGson(baseRequest.getPostData(), User.class);
    Future<User> userAuthFuture = userDA.selectUserByUserName(user.getUsername());
    Future<BaseResponse> future = Future.future();
    userAuthFuture.setHandler(
        event -> {
          if (event.succeeded()) {
            User userAuth = event.result();
            handleLogin(user, future, userAuth);
          } else {
            log.error("login failed by user={}, cause={}", user.getUsername(), event.cause());
            BaseResponse baseResponse =
                BaseResponse.builder()
                    .statusCode(HttpResponseStatus.UNAUTHORIZED.code())
                    .message("Username not existed")
                    .build();
            future.complete(baseResponse);
          }
        });
    return future;
  }

  private void handleLogin(User user, Future<BaseResponse> future, User userAuth) {
    if (BCrypt.checkpw(user.getPassword(), userAuth.getPassword())) {
      String token = jwtUtils.generateToken(userAuth.getUserId());
      LoginDataResponse loginDR =
          LoginDataResponse.builder().token(token).userId(userAuth.getUserId()).build();
      BaseResponse baseResponse =
          BaseResponse.builder()
              .statusCode(HttpResponseStatus.OK.code())
              .message("Login successfully")
              .data(loginDR)
              .build();
      future.complete(baseResponse);
      log.info("login successfully by user={}",userAuth.getUsername());
    } else {
      BaseResponse baseResponse =
          BaseResponse.builder()
              .statusCode(HttpResponseStatus.UNAUTHORIZED.code())
              .message("Invalid password")
              .build();
      future.complete(baseResponse);
      log.info("login failed by user={},cause=invalid.password",userAuth.getUsername());
    }
  }
}
