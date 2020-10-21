package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RSet;
import vn.zalopay.phucvt.fooapp.config.CacheConfig;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Builder
public class UserCacheImpl implements UserCache {
  private static final String METRIC = "RedisCache";

  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;
  private final CacheConfig cacheConfig;

  @Override
  public Future<List<User>> setUserList(List<User> users) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    Future<List<User>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RSet<User> userRSet = redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
            userRSet.addAll(users);
            userRSet.expire(cacheConfig.getExpireUserList(), TimeUnit.MINUTES);
            future.complete(users);
            tracker.step("set-user-list").build().record();
          } catch (Exception e) {
            future.fail(e);
            log.error("add list user to cache failed cause={}", ExceptionUtil.getDetail(e));
          }
        });
    return future;
  }

  @Override
  public Future<User> addToUserList(User user) {
    Future<User> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RSet<User> userRSet = redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
            if (userRSet.size() != 0) {
              userRSet.add(user);
              userRSet.expire(10, TimeUnit.MINUTES);
            }
            future.complete(user);
          } catch (Exception e) {
            log.error(
                "add user={} to cache failed cause={}",
                user.getUserId(),
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<List<User>> getUserList() {
    Future<List<User>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RSet<User> userRSet = redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
            if (userRSet.isEmpty()) {
              future.fail("user list not exist");
            } else {
              future.complete(new ArrayList<>(userRSet.readAll()));
            }
          } catch (Exception e) {
            log.error("get user list in cache failed cause={}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public void setFriendList(List<UserFriendItem> friendList, String userId) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    asyncHandler.run(
        () -> {
          try {
            RSet<UserFriendItem> userRSet =
                redisCache.getRedissonClient().getSet(CacheKey.getFriendListKey(userId));
            userRSet.addAll(friendList);
            userRSet.expire(cacheConfig.getExpireUserList(), TimeUnit.MINUTES);
          } catch (Exception e) {
            log.error(
                "add friend list of userId={} to cache failed cause={}",
                userId,
                ExceptionUtil.getDetail(e));
          }
        });
    tracker.step("set-friend-list").build().record();
  }

  @Override
  public void appendFriendList(UserFriendItem user, String userId) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    asyncHandler.run(
        () -> {
          try {
            RSet<UserFriendItem> userRSet =
                redisCache.getRedissonClient().getSet(CacheKey.getFriendListKey(userId));
            userRSet.add(user);
            userRSet.expire(cacheConfig.getExpireUserList(), TimeUnit.MINUTES);
          } catch (Exception e) {
            log.error(
                "append user={} to friendList of user={} cache failed cause={}",
                user.getId(),
                userId,
                ExceptionUtil.getDetail(e));
          }
        });
    tracker.step("append-friend").build().record();
  }

  @Override
  public Future<List<UserFriendItem>> getFriendList(String userId) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    Future<List<UserFriendItem>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RSet<UserFriendItem> userRSet =
                redisCache.getRedissonClient().getSet(CacheKey.getFriendListKey(userId));
            if (userRSet.isEmpty()) {
              future.fail("friend list empty, user=" + userId);
            } else {
              future.complete(new ArrayList<>(userRSet.readAll()));
            }
          } catch (Exception e) {
            log.error(
                "get friend list of user={} in cache failed cause={}",
                userId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    tracker.step("get-friend-list").build().record();
    return future;
  }
}
