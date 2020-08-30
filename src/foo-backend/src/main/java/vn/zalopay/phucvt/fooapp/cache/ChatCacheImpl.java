package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RList;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Log4j2
public class ChatCacheImpl implements ChatCache {

  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;

  @Override
  public Future<Void> set(WsMessage msg) {
    Future future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messages =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(msg.getSender_id(), msg.getReceiver_id()));
            messages.add(msg);
            if (messages.size() > 100) { // Only store 100 recent messages.
              messages.remove(0);
            }
            messages.expire(10, TimeUnit.MINUTES);
          } catch (Exception e) {
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<List<WsMessage>> getList(String firstUserId, String secondUserId) {
    Future future = Future.future();
    log.info("get user cache list");
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messages =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(firstUserId, secondUserId));
            if (messages.isEmpty()) {
              log.info("Cache failed");
              future.fail("Failed");
            } else {
              log.info("cache exist");
              future.complete(messages.readAll());
              log.info("read done");
            }
          } catch (Exception e) {
            log.info("Cache failed exception with {}", e.getMessage());

            future.fail(e);
          }
        });
    return future;
  }
}
