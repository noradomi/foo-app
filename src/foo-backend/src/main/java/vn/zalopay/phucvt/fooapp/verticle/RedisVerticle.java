package vn.zalopay.phucvt.fooapp.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.RedisOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedisVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LogManager.getLogger(RedisVerticle.class);
    private JsonObject config;

    public RedisVerticle(JsonObject config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Connection to Redis ser");
        String host = config.getString("host","localhost");
//        Integer port = config.getInteger("port",6379);

        String connectionString = String.format("redis://%s",host);

        Redis redis = Redis.createClient(vertx,connectionString)
                .connect(onConnect -> {
                    if(onConnect.succeeded()){
                        LOGGER.info("Connect to Redis server successfully");
                        RedisConnection redisClient = onConnect.result();
                        RedisAPI redisAPI = RedisAPI.api(redisClient);
                        redisAPI.get("user",res -> {
                            if(res.succeeded()){
                                System.out.println(res.result());
                            }
                        });
                    }
                });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
