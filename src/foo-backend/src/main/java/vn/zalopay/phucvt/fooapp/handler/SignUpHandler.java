package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

@Builder
@Log4j2
public class SignUpHandler extends BaseHandler {
  private final UserCache userCache;
  private final UserDA userDA;
  private final WSHandler wsHandler;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    Future<BaseResponse> future = Future.future();
    final User user = JsonProtoUtils.parseGson(baseRequest.getPostData(), User.class);
    //        Validates post data
    Future<BaseResponse> validatePostDataFuture = validatePostData(future, user);
    if (validatePostDataFuture != null) return validatePostDataFuture;
    Future<User> userAuthFuture = userDA.selectUserByUserName(user.getUsername());
    userAuthFuture.setHandler(
        userAsyncResult -> {
          if (userAsyncResult.succeeded()) {
            User existedUserAuth = userAsyncResult.result();
            if (existedUserAuth != null) {
              handleException(future, "User name existed");
            } else {
              user.setUserId(GenerationUtils.generateId());
              String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
              user.setPassword(hashedPassword);
              userDA
                  .insert(user)
                  .setHandler(
                      event -> {
                        if (event.failed()) {
                          log.error("insert user to db failed", event.cause());
                        } else {
                          userCache
                              .addToUserList(user)
                              .setHandler(
                                  userCacheResult -> {
                                    if (userCacheResult.succeeded()) {
                                      BaseResponse response =
                                          BaseResponse.builder()
                                              .statusCode(HttpResponseStatus.OK.code())
                                              .data(userCacheResult.result())
                                              .message("Sign up successfully")
                                              .build();
                                      future.complete(response);
                                    }
                                  });
                        }
                      });
            }
          } else {
            log.info("get user auth failed, cause", userAsyncResult.cause());
          }
        });
    return future;
  }

  private Future<BaseResponse> validatePostData(Future<BaseResponse> future, User user) {
    if (StringUtils.isBlank(user.getUsername())) {
      return handleException(future, "User name cannot be empty");
    }
    if (StringUtils.isBlank(user.getPassword())) {
      return handleException(future, "Password cannot be empty");
    }
    if (StringUtils.isBlank(user.getName())) {
      return handleException(future, "Full name cannot be empty");
    }
    return null;
  }

  private Future<BaseResponse> handleException(Future<BaseResponse> future, String message) {
    BaseResponse response =
        BaseResponse.builder()
            .statusCode(HttpResponseStatus.BAD_REQUEST.code())
            .message(message)
            .build();
    future.complete(response);
    return future;
  }
}
