package vn.zalopay.phucvt.fooapp.cache;

import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

@Getter
public class RedisCache {
  private RedisCache() {}

  private RedissonClient redissonClient;

  public static RedisCache newInstance() throws IOException {
    Config config = Config.fromYAML(new File(System.getProperty("redis.conf")));
    RedisCache redisCache = new RedisCache();
    redisCache.redissonClient = Redisson.create(config);
    return redisCache;
  }

  public void dispose() {
    if (redissonClient != null) {
      redissonClient.shutdown();
    }
  }
}
