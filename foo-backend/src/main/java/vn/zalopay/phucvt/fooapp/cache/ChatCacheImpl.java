package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RList;
import vn.zalopay.phucvt.fooapp.config.CacheConfig;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Log4j2
public class ChatCacheImpl implements ChatCache {
  private static final String METRIC = "RedisCache";

  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;
  private final CacheConfig cacheConfig;

  @Override
  public Future<WsMessage> addToList(WsMessage message) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    Future<WsMessage> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messageRList =
                redisCache
                    .getRedissonClient()
                    .getList(
                        CacheKey.getMessageKey(message.getSenderId(), message.getReceiverId()));
            messageRList.add(message);
            if (messageRList.size() > cacheConfig.getMaxMessagesSize()) {
              messageRList.remove(0);
            }
            messageRList.expire(cacheConfig.getExpireMessages(), TimeUnit.MINUTES);
            future.complete(message);
          } catch (Exception e) {
            log.error("add a message to cache failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    tracker.step("append-message").build().record();
    return future;
  }

  @Override
  public Future<List<WsMessage>> addMessageList(
      List<WsMessage> messageList, String firstUserId, String secondUserId) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    Future<List<WsMessage>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messageRList =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(firstUserId, secondUserId));
            messageRList.addAll(messageList);
            messageRList.expire(cacheConfig.getExpireMessages(), TimeUnit.MINUTES);
            future.complete(messageList);
          } catch (Exception e) {
            log.error("add message list to cache failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    tracker.step("set-message-list").build().record();
    return future;
  }

  @Override
  public Future<List<WsMessage>> getMessageList(String firstUserId, String secondUserId) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    Future<List<WsMessage>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RList<WsMessage> messageRList =
                redisCache
                    .getRedissonClient()
                    .getList(CacheKey.getMessageKey(firstUserId, secondUserId));
            if (!messageRList.isExists()) {
              future.fail("message list not exist");
            } else {
              future.complete(messageRList.readAll());
            }
          } catch (Exception e) {
            log.error("get message list in cache failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    tracker.step("get-message-list").build().record();
    return future;
  }
}
