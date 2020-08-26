package vn.zalopay.phucvt.fooapp.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.api.ApiServer;

public class FooVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(FooVerticle.class);

    private ApiServer apiServer;

    private JsonObject config;

    public FooVerticle(JsonObject config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("{} verticle {} start",deploymentID(),Thread.currentThread().getName());
        this.apiServer = ApiServer.getInstance(config);

        apiServer.createHttpServer(vertx);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Shutting down application");
    }
}
