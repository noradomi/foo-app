package vn.zalopay.phucvt.fooapp.cache;

import vn.zalopay.phucvt.fooapp.model.User;
import io.vertx.core.Future;

import java.util.List;

public interface UserCache {
  Future<User> set(User user);

  Future<User> get (String userId);

  Future<Boolean> del(String userId);

  Future<Void> setUserList(List<User> user);

  Future<List<User>> getUserList();
}
