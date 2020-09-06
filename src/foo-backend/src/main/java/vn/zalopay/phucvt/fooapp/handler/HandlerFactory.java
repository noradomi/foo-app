package vn.zalopay.phucvt.fooapp.handler;

import com.google.common.collect.ImmutableMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.config.APIPath;

@Builder
public class HandlerFactory {
  private BaseHandler loginHandler;
  private BaseHandler signUpHandler;
  private BaseHandler signOutHandler;
  private AuthHandler authHandler;
  private BaseHandler userListHanlder;
  private BaseHandler messageListHandler;

  public void initialize(Router router) {
    router.route("/api/protected/*").handler(authHandler::handle);
    router
        .route("/api/protected/messages/:friendId/:offset")
        .method(HttpMethod.GET)
        .handler(messageListHandler::handle);

    ImmutableMap<String, BaseHandler> getHandler =
        ImmutableMap.<String, BaseHandler>builder().put(APIPath.USER_LIST, userListHanlder).build();

    ImmutableMap<String, BaseHandler> postHandler =
        ImmutableMap.<String, BaseHandler>builder()
            .put(APIPath.LOGIN, loginHandler)
            .put(APIPath.SIGNUP, signUpHandler)
            .put(APIPath.LOGOUT, signOutHandler)
            .build();

    getHandler
        .entrySet()
        .forEach(
            entry ->
                router
                    .route()
                    .method(HttpMethod.GET)
                    .path(entry.getKey())
                    .handler(entry.getValue()::handle));

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
