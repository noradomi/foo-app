package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RList;
import org.redisson.api.RQueue;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Log4j2
public class ChatCacheImpl implements ChatCache {

  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;

  @Override
  public Future<WsMessage> set(WsMessage msg) {
    log.info("> Insert message to cache");
    Future<WsMessage> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messages =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(msg.getSender_id(), msg.getReceiver_id()));
            messages.add(msg);
            if (messages.size() > 20) { // Only store 100 recent messages.
                List<WsMessage> msgList = messages.readAll();
                msgList.sort(Comparator.comparing(WsMessage::getCreate_date));
                msgList.remove(0);
                messages.clear();
                messages.addAll(msgList);
            }
            messages.expire(10, TimeUnit.MINUTES);
            future.complete(msg);
          } catch (Exception e) {
            future.fail(e);
          }
        });

    return future;
  }

  @Override
  public Future<List<WsMessage>> getList(String firstUserId, String secondUserId) {
    Future<List<WsMessage>> future = Future.future();
    log.info("> Get user list from cache");
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messages =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(firstUserId, secondUserId));
//            RQueue<String> a = redisCache.getRedissonClient().getQueue("a");
//            a.
            if (messages.isEmpty()) {

              future.fail("Failed");
            } else {
              future.complete(messages.readAll());
            }
          } catch (Exception e) {
            log.info("Cache failed exception with {}", e.getMessage());

            future.fail(e);
          }
        });
    return future;
  }
}