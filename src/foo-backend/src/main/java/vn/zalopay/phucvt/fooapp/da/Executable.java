package vn.zalopay.phucvt.fooapp.da;


import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;

public interface Executable<R> {
  Future<R> execute(SQLConnection connection);
}
