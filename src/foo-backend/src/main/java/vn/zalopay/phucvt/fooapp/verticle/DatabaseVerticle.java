package vn.zalopay.phucvt.fooapp.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLConnection;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DatabaseVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseVerticle.class);

    private JsonObject config;

    public DatabaseVerticle(JsonObject config){
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Connection to MySql server");

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(config.getInteger("port",3306))
                .setHost(config.getString("host","localhost"))
                .setDatabase(config.getString("database","fooapp"))
                .setUser(config.getString("user","root"))
                .setPassword(config.getString("password","1234"))
                .setCharset("utf8mb4")
                .setCollation("utf8mb4_unicode_ci");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        MySQLPool client = MySQLPool.pool(vertx, connectOptions, poolOptions);

        client.getConnection(ar -> {
            if (ar.succeeded()) {
                System.out.println("Connected");
                SqlConnection conn = ar.result();
                conn
                        .query("SHOW DATABASES;")
                        .execute(ar2 -> {
                            if (ar2.succeeded()) {
                                RowSet<Row> result = ar2.result();
                                System.out.println("Got " + result.size() + " rows ");
                            } else {
                                System.out.println("Failure: " + ar.cause().getMessage());
                            }
                            client.close();
                        });
            }
            else {
                    System.out.println("Could not connect: " + ar.cause().getMessage());
                }
        });
    }
}