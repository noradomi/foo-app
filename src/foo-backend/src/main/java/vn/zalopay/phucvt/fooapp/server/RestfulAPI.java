package vn.zalopay.phucvt.fooapp.server;

import vn.zalopay.phucvt.fooapp.handler.HandlerFactory;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import lombok.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public class RestfulAPI {
    private static final Logger LOGGER =
            LogManager.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());
    private Vertx vertx;
    private int port;
    private HttpServer httpServer;
    private JWTAuth authProvider;
    private HandlerFactory handlerFactory;

    @Builder
    public RestfulAPI(Vertx vertx, int port, JWTAuth authProvider, HandlerFactory handlerFactory) {
        this.vertx = vertx;
        this.port = port;
        this.authProvider = authProvider;
        this.handlerFactory = handlerFactory;
    }

    public Future<Void> start() {
        LOGGER.info("Starting RestfulAPI Server ...");

        Router router = RouterFactory.route(vertx, authProvider, handlerFactory);

        Future future = Future.future();
        httpServer =
                vertx
                        .createHttpServer()
                        .requestHandler(router)
                        .exceptionHandler(
                                e -> LOGGER.error("Handle request exception", ExceptionUtil.getDetail(e)))
                        .listen(
                                port,
                                ar -> {
                                    if (ar.succeeded()) {
                                        LOGGER.info("API Server start successfully !, port:{}", port);
                                        future.complete();
                                    } else {
                                        LOGGER.error("API Server start fail. Reason: {}", ar.cause().getMessage());
                                        future.fail(ar.cause());
                                    }
                                });

        return future;
    }

    public void close() {
        httpServer.close();
    }
}
