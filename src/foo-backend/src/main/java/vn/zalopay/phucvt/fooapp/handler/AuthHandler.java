package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;

/*
* A middleware for authenticating all requests to path /api/protected/* include:
    - Authenticate JWT token
    - Check token exist in blacklist cache.
*/

@Builder
@Log4j2
public class AuthHandler {
  private final BlackListCache blackListCache;

  private final JWTAuth authProvider;

  public void handle(RoutingContext rc) {

    HttpServerRequest request = rc.request();
    HttpServerResponse response = rc.response();

    String token =
        request.headers().get(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
    authProvider.authenticate(
        new JsonObject().put("jwt", token),
        res -> { // res is always succeed
          Future<Boolean> checkExistInBlackList = blackListCache.constains(token);
          checkExistInBlackList.compose(
              exist -> {
                if (exist) {
                  response
                      .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                      .putHeader("content-type", "application/json; charset=utf-8")
                      .end("Token have been blacklist");
                } else {
                  rc.next();
                }
                  return null;
              });
        });
  }
}
