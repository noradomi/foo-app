package vn.zalopay.phucvt.fooapp;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.util.ConfigLoader;
import vn.zalopay.phucvt.fooapp.verticle.DatabaseVerticle;
import vn.zalopay.phucvt.fooapp.verticle.FooVerticle;
import vn.zalopay.phucvt.fooapp.verticle.RedisVerticle;

public class FooApp {
    private static final Logger LOGGER = LogManager.getLogger(FooApp.class);
    public static void main(String[] args) {
        LOGGER.info("Start Application");
        Vertx vertx = Vertx.vertx();

//        Load config
        ConfigRetriever configRetriever = ConfigLoader.load(vertx);
        configRetriever.getConfig(ar -> handleConfig(vertx,ar));
    }

    private static void handleConfig(Vertx vertx, AsyncResult<JsonObject> jsonResult){
        if(jsonResult.succeeded()){
            LOGGER.info("Load configuration successfully");
            injectConfig(vertx,jsonResult.result());
        }
        else{
            LOGGER.info("Error when load configuration",jsonResult.cause());
            vertx.close();
        }
    }

    private static void injectConfig(Vertx vertx,JsonObject configuration){
//        vertx.deployVerticle(new DatabaseVerticle(configuration.getJsonObject("mysql")));
//        vertx.deployVerticle(new RedisVerticle(configuration.getJsonObject("redis")));
        vertx.deployVerticle(new FooVerticle(configuration.getJsonObject("server")));
    }
}
