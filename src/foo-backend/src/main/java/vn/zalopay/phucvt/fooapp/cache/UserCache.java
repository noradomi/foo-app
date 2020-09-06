package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.User;

import java.util.List;

public interface UserCache {
  Future<List<User>> setUserList(List<User> user);
  Future<User> addToUserList(User user);
  Future<List<User>> getUserList();
}
