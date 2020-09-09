package vn.zalopay.phucvt.fooapp.da;

import io.vertx.ext.jdbc.JDBCClient;
import vn.zalopay.phucvt.fooapp.config.MySQLConfig;

import javax.sql.DataSource;

public interface DataSourceProvider {
  JDBCClient getVertxDataSource(MySQLConfig config);

  DataSource getDataSource(MySQLConfig config);
}
