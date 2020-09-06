package vn.zalopay.phucvt.fooapp.server;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.handler.HandlerFactory;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

@Log4j2
public class RestfulAPI {
  private final Vertx vertx;
  private final int port;
  private HttpServer httpServer;
  private final JWTAuth authProvider;
  private final HandlerFactory handlerFactory;

  @Builder
  public RestfulAPI(Vertx vertx, int port, JWTAuth authProvider, HandlerFactory handlerFactory) {
    this.vertx = vertx;
    this.port = port;
    this.authProvider = authProvider;
    this.handlerFactory = handlerFactory;
  }

  public Future<Void> start() {
    Router router = RouterFactory.route(vertx, authProvider, handlerFactory);

    Future<Void> future = Future.future();
    httpServer =
        vertx
            .createHttpServer(
                new HttpServerOptions()
                    .setTcpKeepAlive(true)
                    .setMaxHeaderSize(32 * 1024)
                    .setLogActivity(true))
            .requestHandler(router)
            .exceptionHandler(
                e -> log.error("Handle request exception {}", ExceptionUtil.getDetail(e)))
            .listen(
                port,
                ar -> {
                  if (ar.succeeded()) {
                    log.info("API Server start successfully !, port:{}", port);
                    future.complete();
                  } else {
                    log.error("API Server start fail. Reason: {}", ar.cause().getMessage());
                    future.fail(ar.cause());
                  }
                });

    return future;
  }

  public void close() {
    httpServer.close();
  }
}
