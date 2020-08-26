package vn.zalopay.phucvt.fooapp;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.util.ConfigUtils;
import vn.zalopay.phucvt.fooapp.verticle.DatabaseVerticle;
import vn.zalopay.phucvt.fooapp.verticle.FooVerticle;
import vn.zalopay.phucvt.fooapp.verticle.RedisVerticle;

public class FooApp {
    private static final Logger LOGGER = LogManager.getLogger(FooApp.class);
    public static void main(String[] args) {
        LOGGER.info("Start Application");
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new FooVerticle());

//        Load config
//         ConfigUtils.load(vertx);

    }



//    private static void injectConfig(Vertx vertx,JsonObject configuration){
//        vertx.deployVerticle(new DatabaseVerticle(configuration.getJsonObject("mysql")));
//        vertx.deployVerticle(new RedisVerticle(configuration.getJsonObject("redis")));
//        vertx.deployVerticle(new FooVerticle(configuration.getJsonObject("server")));
//    }
}
