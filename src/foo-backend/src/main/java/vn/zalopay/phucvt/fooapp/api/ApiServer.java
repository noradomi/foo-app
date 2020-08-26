package vn.zalopay.phucvt.fooapp.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.handler.WebHandler;

import java.util.HashSet;
import java.util.Set;

public final class ApiServer {

    private HttpServer httpServer;

    private WebHandler webHandler;

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

        router.route().handler(BodyHandler.create());

//                Set up API
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("accept");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("Authorization");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.PUT);
        allowedMethods.add(HttpMethod.DELETE);

        router.route("/*").handler(CorsHandler.create("http://localhost:3000")
        .allowedHeaders(allowedHeaders)
        .allowedMethods(allowedMethods)
        .allowCredentials(true))
                .handler(BodyHandler.create());

        router.post("/signin").handler( routingContext -> {
            HttpServerResponse httpServerResponse = routingContext.response();
            httpServerResponse.putHeader("content-ytpe", "text/html")
                    .end("<h1> Log In Successfully !</h1>");
        });

        Future future = Future.future();

        httpServer = vertx
                .createHttpServer()
                .requestHandler(router)
                .exceptionHandler(exHandler -> {
                    LOGGER.error(exHandler.getCause());
                })
                .listen(
                        config.getInteger("port",8080),"localhost",
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
