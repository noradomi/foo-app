package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import vn.zalopay.phucvt.fooapp.config.CacheConfig;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Builder
public class UserCacheImpl implements UserCache {
  private final RedisCache redisCache;
  private final AsyncHandler asyncHandler;
  private final CacheConfig cacheConfig;
  //
  //  @Override
  //  public Future<User> set(User user) {
  //    Future<User> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            RMap<Object, Object> userMap =
  //                redisCache.getRedissonClient().getMap(CacheKey.getUserKey(user.getUserId()));
  //            userMap.put("userId", user.getUserId());
  //            userMap.put("username", user.getUsername());
  //            userMap.put("name", user.getName());
  //            future.complete(user);
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }
  //
  //  @Override
  //  public Future<User> get(String userId) {
  //    Future<User> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            RMap<String, String> userMap =
  //                redisCache.getRedissonClient().getMap(CacheKey.getUserKey(userId));
  //            User user =
  //
  // User.builder().username(userMap.get("username")).name(userMap.get("name")).build();
  //            future.complete(user);
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }
  //
  //  @Override
  //  public Future<Boolean> del(String userId) {
  //    Future<Boolean> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            String onlineUserKey = CacheKey.getUserKey(userId);
  //            long res = redisCache.getRedissonClient().getKeys().delete(onlineUserKey);
  //            future.complete(res != -1);
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }

  @Override
  public Future<List<User>> setUserList(List<User> users) {
    Future<List<User>> future = Future.future();
    asyncHandler.run(
        () -> {
          try {
            RSet<User> userRSet = redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
            userRSet.addAll(users);
            userRSet.expire(cacheConfig.getExpireUserList(), TimeUnit.MINUTES);
            future.complete(users);
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
            RSet<User> userRSet =
                redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
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
            RSet<User> userRSet =
                redisCache.getRedissonClient().getSet(CacheKey.getUserListKey());
            if (userRSet.isEmpty()) {
              future.fail("user list not exist");
            } else {
              future.complete(new ArrayList<>(userRSet.readAll()));
            }
          } catch (Exception e) {
              e.printStackTrace();
            log.error("get user list in cache failed cause={}" );
            future.fail(e);
          }
        });
    return future;
  }

  //  @Override
  //  public Future<Void> setOnlineUserStatus(String userId) {
  //    Future<Void> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            RBucket<Integer> userStatus =
  //                redisCache.getRedissonClient().getBucket(CacheKey.getUserStatusKey(userId));
  //            userStatus.set(1);
  //            future.complete();
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }
  //
  //  @Override
  //  public Future<Void> delOnlineUserStatus(String userId) {
  //    Future<Void> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            RBucket<Integer> userStatus =
  //                redisCache.getRedissonClient().getBucket(CacheKey.getUserStatusKey(userId));
  //            userStatus.delete();
  //            future.complete();
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }
  //
  //  @Override
  //  public Future<Boolean> isOnlineStatus(String userId) {
  //    Future<Boolean> future = Future.future();
  //    asyncHandler.run(
  //        () -> {
  //          try {
  //            RBucket<Integer> userStatus =
  //                redisCache.getRedissonClient().getBucket(CacheKey.getUserStatusKey(userId));
  //            future.complete(userStatus.get() != null);
  //          } catch (Exception e) {
  //            future.fail(e);
  //          }
  //        });
  //    return future;
  //  }
}
