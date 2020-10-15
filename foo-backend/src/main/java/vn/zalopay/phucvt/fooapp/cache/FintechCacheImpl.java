package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RDeque;
import vn.zalopay.phucvt.fooapp.config.CacheConfig;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Log4j2
public class FintechCacheImpl implements FintechCache {
  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;
  private final CacheConfig cacheConfig;

  @Override
  public void setTransactionHistory(List<HistoryItem> items, String userId) {
    asyncHandler.run(
        () -> {
          try {
            RDeque<HistoryItem> historySet =
                redisCache
                    .getRedissonClient()
                    .getDeque(CacheKey.getTrasactionHistoryListKey(userId));
            historySet.addAll(items);
            historySet.expire(cacheConfig.getExpireTransactionHistory(), TimeUnit.MINUTES);
          } catch (Exception e) {
            log.error(
                "cache transaction history of userId={}  failed, cause={}",
                userId,
                ExceptionUtil.getDetail(e));
          }
        });
  }

  @Override
  public void appendToTransactionHistory(HistoryItem item, String userId) {
    asyncHandler.run(
        () -> {
          try {
            RDeque<HistoryItem> historySet =
                redisCache
                    .getRedissonClient()
                    .getDeque(CacheKey.getTrasactionHistoryListKey(userId));
            historySet.addFirst(item);
            if (historySet.size() > cacheConfig.getMaxTransactionHistorySize()) {
              historySet.pollLast();
            }
            historySet.expire(cacheConfig.getExpireTransactionHistory(), TimeUnit.MINUTES);
          } catch (Exception e) {
            log.error(
                "append to transaction history of user={} cache failed, cause={}",
                userId,
                ExceptionUtil.getDetail(e));
          }
        });
  }

  @Override
  public Future<List<HistoryItem>> getTransactionHistory(String userId) {
    Future<List<HistoryItem>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RDeque<HistoryItem> historySet =
                redisCache
                    .getRedissonClient()
                    .getDeque(CacheKey.getTrasactionHistoryListKey(userId));
            if (historySet.isEmpty()) {
              future.fail("transaction history cache empty, user=" + userId);
            } else {
              future.complete(historySet.readAll());
            }
          } catch (Exception e) {
            log.error(
                "get transaction history of user={} in cache failed cause={}",
                userId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }
}
