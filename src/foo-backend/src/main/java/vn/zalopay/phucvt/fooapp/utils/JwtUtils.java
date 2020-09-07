package vn.zalopay.phucvt.fooapp.utils;

import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.config.JwtConfig;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;

@Builder
@Log4j2
public class JwtUtils {
  private final JWTAuth jwtAuth;
  private final JwtConfig jwtConfig;

  public String generateToken(String userId) {
    return jwtAuth.generateToken(
        new JsonObject().put("userId", userId),
        new JWTOptions().setExpiresInSeconds(jwtConfig.getExpireToken()));
  }

  public String getTokenFromHeader(RoutingContext routingContext) {
    return routingContext
        .request()
        .getHeader(HttpHeaders.AUTHORIZATION)
        .substring("Bearer ".length());
  }

  public Future<String> getUserIdFromToken(BaseRequest baseRequest) {
    String token =
        baseRequest
            .getHeaders()
            .get(HttpHeaders.AUTHORIZATION)
            .substring("Bearer ".length())
            .trim();
    Future<String> userIdFuture = Future.future();
    jwtAuth.authenticate(
        new JsonObject().put("jwt", token),
        event -> {
          userIdFuture.complete(event.result().principal().getString("userId"));
        });
    return userIdFuture;
  }

  public Future<String> authenticate(String token) {
    Future<String> future = Future.future();
    jwtAuth.authenticate(
        new JsonObject().put("jwt", token),
        event -> {
          if (event.succeeded()) {
            String userId = event.result().principal().getString("userId");
            future.complete(userId);
          } else {
            log.error("authenticate jwt failed", event.cause());
            future.fail(event.cause());
          }
        });
    return future;
  }
}
