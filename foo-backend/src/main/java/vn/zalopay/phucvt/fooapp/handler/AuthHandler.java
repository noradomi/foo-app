package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.JwtUtils;

@Builder
@Log4j2
public class AuthHandler {
  private final BlackListCache blackListCache;
  private final JwtUtils jwtUtils;

  public void handle(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    String token = jwtUtils.getTokenFromHeader(routingContext);

    Future<Boolean> isExistFuture = blackListCache.contains(token);

    isExistFuture.setHandler(
        resAsync -> {
          if (resAsync.succeeded()) {
            if (resAsync.result()) {
              response
                  .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                  .end("Token have been blacklist");
            } else {
              routingContext.next();
            }
          } else {
            log.error(
                "error when check token blacklist, cause={}",
                ExceptionUtil.getDetail(resAsync.cause()));
            routingContext.next();
          }
        });
  }
}
