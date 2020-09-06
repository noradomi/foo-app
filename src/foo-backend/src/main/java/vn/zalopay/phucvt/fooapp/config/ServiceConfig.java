package vn.zalopay.phucvt.fooapp.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceConfig {
  private VertxConfig vertxConfig;
  private MySQLConfig mySQLConfig;
  private CacheConfig cacheConfig;
  private JwtConfig jwtConfig;
  int port;
  int wsPort;
}
