package vn.zalopay.phucvt.fooapp.cache;

import vn.zalopay.phucvt.fooapp.config.FileConfigLoader;
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
//    Config config = Config.fromYAML(new File(System.getProperty("redis.conf")));
    ClassLoader classLoader = FileConfigLoader.class.getClassLoader();

    File file = new File(classLoader.getResource("conf/redis.yaml").getFile());
    Config config = Config.fromYAML(file);
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
