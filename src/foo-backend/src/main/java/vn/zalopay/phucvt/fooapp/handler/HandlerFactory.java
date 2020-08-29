package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.config.APIPath;
import com.google.common.collect.ImmutableMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import lombok.Builder;

@Builder
public class HandlerFactory {
  private BaseHandler echoHandler;
  private BaseHandler exampleHandler;
  private BaseHandler loginHandler;
  private BaseHandler signUpHandler;
  private BaseHandler signOutHandler;
  private AuthHandler authHandler;

  public void initialize(Router router) {

    router.route("/api/protected/*").handler(authHandler::handle);

    ImmutableMap<String, BaseHandler> postHandler =
        ImmutableMap.<String, BaseHandler>builder()
            .put(APIPath.ECHO, echoHandler)
            .put(APIPath.EXAMPLE, exampleHandler)
            .put(APIPath.LOGIN, loginHandler)
            .put(APIPath.SIGNUP, signUpHandler)
            .put(APIPath.SIGNOUT, signOutHandler)
            .build();

    postHandler
        .entrySet()
        .forEach(
            entry ->
                router
                    .route()
                    .method(HttpMethod.POST)
                    .path(entry.getKey())
                    .handler(entry.getValue()::handle));
  }
}
