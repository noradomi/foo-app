package vn.zalopay.phucvt.fooapp.da;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import vn.zalopay.phucvt.fooapp.config.MySQLConfig;

import javax.sql.DataSource;

public class DataSourceProviderImpl implements DataSourceProvider {
  private final Vertx vertx;

  public DataSourceProviderImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public JDBCClient getVertxDataSource(MySQLConfig config) {

    return JDBCClient.create(vertx, getDataSource(config));
  }

  @Override
  public DataSource getDataSource(MySQLConfig config) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(config.getDriver());
    hikariConfig.setJdbcUrl(config.getUrl());
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getPassword());
    hikariConfig.setMaximumPoolSize(config.getPoolSize());
    hikariConfig.setAutoCommit(config.isAutoCommit());
    hikariConfig.addDataSourceProperty("useServerPrepStmts", "" + config.isUseServerPrepStmts());
    hikariConfig.addDataSourceProperty("cachePrepStmts", "" + config.isCachePrepStmts());
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "" + config.getPrepStmtCacheSize());
    hikariConfig.addDataSourceProperty(
        "prepStmtCacheSqlLimit", "" + config.getPrepStmtCacheSqlLimit());
    hikariConfig.addDataSourceProperty("maxLifetime", "" + config.getMaxLifetimeMillis());
    hikariConfig.setLeakDetectionThreshold(30000);
    hikariConfig.setConnectionTimeout(30000);
    //    hikariConfig.setMetricRegistry(Tracker.getMeterRegistry());
    return new HikariDataSource(hikariConfig);
  }
}
