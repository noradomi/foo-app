package vn.zalopay.phucvt.fooapp.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/** Created by thuyenpt Date: 2019-11-13 */
@Getter
@Setter
public class MySQLConfig implements Serializable {
  private String driver = "";

  private transient String username = "";
  private transient String password = "";

  private String url = "";
  private int poolSize = 2;
  private int maxLifetimeMillis = 600000;
  private boolean cachePrepStmts = true;
  private boolean useServerPrepStmts = true;
  private int prepStmtCacheSize = 250;
  private int prepStmtCacheSqlLimit = 2048;
  private boolean autoCommit = true;
}
