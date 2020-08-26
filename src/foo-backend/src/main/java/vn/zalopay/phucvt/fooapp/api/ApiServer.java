package vn.zalopay.phucvt.fooapp.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ApiServer {

    private HttpServer httpServer;

    private JsonObject config;

    private static final Logger LOGGER = LogManager.getLogger(ApiServer.class);

    private static ApiServer apiServer;

    private ApiServer(JsonObject config) {
        this.config = config;
    }

    public static ApiServer getInstance(JsonObject config){
        if(apiServer == null){
            apiServer = new ApiServer(config);
        }
        return apiServer;
    }


    public Future<Void> createHttpServer(Vertx vertx) {
        if (httpServer != null) Future.succeededFuture();

        LOGGER.info("Starting API Server ...");

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse httpServerResponse = routingContext.response();
            httpServerResponse.putHeader("content-ytpe", "text/html")
                    .end("<h1> Hello Noradomi</h1>");
        });

        Future future = Future.future();

        httpServer = vertx
                .createHttpServer()
                .requestHandler(router)
                .exceptionHandler(exHandler -> {
                    LOGGER.error(exHandler.getCause());
                })
                .listen(
                        config.getInteger("port",8080),
                        ar -> {
                            if(ar.succeeded()){
                                LOGGER.info("API Server start successfully !");
                            } else{
                                LOGGER.error("Error when start API Server. Cause by {}",ar.cause().getMessage());
                            }
                        }
                );
        return future;
    }
}
