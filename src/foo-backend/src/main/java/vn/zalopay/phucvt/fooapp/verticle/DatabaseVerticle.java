package vn.zalopay.phucvt.fooapp.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLConnection;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

public class DatabaseVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx= Vertx.vertx();

    }

    @Override
    public void start() throws Exception {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("localhost")
                .setDatabase("fooapp")
                .setUser("root")
                .setPassword("1234");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        MySQLPool client = MySQLPool.pool(vertx, connectOptions, poolOptions);

        client.getConnection(ar -> {
            if (ar.succeeded()) {
                System.out.println("Connected");
                SqlConnection conn = ar.result();
                conn
                        .query("SELECT * FROM user")
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