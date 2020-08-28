package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import java.util.concurrent.TimeUnit;

@Log4j2
@Builder
public class BlackListCacheImpl implements BlackListCache {

    private final RedisCache redisCache;
    private final AsyncHandler asyncHandler;

    @Override
    public Future<String> set(String token,Long ttl) {
//        Set JWT token log out with ttl in cache is token's ttl
        Future<String> future = Future.future();
        asyncHandler.run(
                () -> {
                    try {
                        RBucket<String> blacklist = redisCache
                                .getRedissonClient()
                                .getBucket(CacheKey.getBlacklistKey(token));
                        blacklist.set("1");
                        blacklist.expire(ttl, TimeUnit.MILLISECONDS);
                        log.info("done add token");
                        future.complete(token);

                    } catch (Exception e) {
                        future.fail(e);
                    }
                });
        return future;
    }

    @Override
    public Future<Boolean> constains(String token) {
        Future<Boolean> future = Future.future();
        asyncHandler.run(
                () -> {
                    try {
                        RBucket<String> blacklist = redisCache
                                .getRedissonClient()
                                .getBucket(CacheKey.getBlacklistKey(token));
                        future.complete(blacklist.isExists());
                    } catch (Exception e) {
                        future.fail(e);
                    }
                });
        return future;
    }
}
