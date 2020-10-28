package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;

import java.util.Date;

@Builder
@Log4j2
public class LogOutHandler extends BaseHandler {
  private final BlackListCache blackListCache;
  private final UserCache userCache;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    Future<BaseResponse> future = Future.future();
    String token =
        baseRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).substring(7); // "Bearer "
    long expireTimeInMilliseconds = baseRequest.getPrincipal().getLong("exp") * 1000;
    log.debug(expireTimeInMilliseconds);
    long timeToLive = expireTimeInMilliseconds - new Date().getTime();
    if (timeToLive > 0) {
      blackListCache.set(token, timeToLive);
    }
    BaseResponse baseResponse =
        BaseResponse.builder()
            .statusCode(HttpResponseStatus.OK.code())
            .message("Log out successfully")
            .build();
    future.complete(baseResponse);

    return future;
  }
}
