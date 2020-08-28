package vn.zalopay.phucvt.fooapp.cache;

import vn.zalopay.phucvt.fooapp.model.User;
import io.vertx.core.Future;

public interface UserCache {
  Future<User> set(User user);
}
