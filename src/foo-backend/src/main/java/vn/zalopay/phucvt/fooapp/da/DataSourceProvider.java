package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.config.MySQLConfig;
import io.vertx.ext.jdbc.JDBCClient;

import javax.sql.DataSource;

public interface DataSourceProvider {
  JDBCClient getVertxDataSource(MySQLConfig config);

  DataSource getDataSource(MySQLConfig config);
}
