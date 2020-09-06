package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBucket;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import java.util.concurrent.TimeUnit;

@Log4j2
@Builder
public class BlackListCacheImpl implements BlackListCache {

  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;

  @Override
  public Future<String> set(String token, Long timeToLive) {
    Future<String> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RBucket<String> blacklist =
                redisCache.getRedissonClient().getBucket(CacheKey.getBlacklistKey(token));
            blacklist.set("1");
            blacklist.expire(timeToLive, TimeUnit.MILLISECONDS);
            future.complete(token);
          } catch (Exception e) {
            log.error("set a token to blacklist failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<Boolean> contains(String token) {
    Future<Boolean> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RBucket<String> blacklist =
                redisCache.getRedissonClient().getBucket(CacheKey.getBlacklistKey(token));
            future.complete(blacklist.get() != null);
          } catch (Exception e) {
            log.error("check token blacklist failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }
}
