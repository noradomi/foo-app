package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

@Builder
public class BlackListCacheImpl implements BlackListCache {

    private final RedisCache redisCache;
    private final AsyncHandler asyncHandler;

    @Override
    public Future<String> add(String userId) {
        Future<String> future = Future.future();
        asyncHandler.run(
                () -> {
                    try {
                        RSet<String> blacklist = redisCache
                                .getRedissonClient()
                                .getSet(CacheKey.getBlacklist());
                        blacklist.add(userId);
                        future.complete(userId);
                    } catch (Exception e) {
                        future.fail(e);
                    }
                });
        return future;
    }

    @Override
    public Future<Boolean> contains(String userId) {
        Future<Boolean> future = Future.future();
        asyncHandler.run(
                () -> {
                    try {
                        RSet<String> blacklist = redisCache
                                .getRedissonClient()
                                .getSet(CacheKey.getBlacklist());
                        future.complete(blacklist.contains(userId));
                    } catch (Exception e) {
                        future.fail(e);
                    }
                });
        return future;
    }
}
