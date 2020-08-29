package vn.zalopay.phucvt.fooapp.utils;

import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;

@Builder
public class JWTUtils{
  private JWTAuth jwtAuth;

  public Future<String> getUserIdFromToken(BaseRequest baseRequest) {
    String token =
        baseRequest
            .getHeaders()
            .get(HttpHeaders.AUTHORIZATION)
            .substring("Bearer ".length())
            .trim();
    Future<String> userIdFuture = Future.future();
    jwtAuth.authenticate(new JsonObject().put("jwt", token), event -> {
      userIdFuture.complete(event.result().principal().getString("userId"));
    });
    return userIdFuture;
  }
}
