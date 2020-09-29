package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.User;

import java.util.List;

public interface UserDA {
  Future<Void> insert(User user);

  Future<User> selectUserById(String id);

  Future<User> selectUserByUserName(String username);

  Future<List<User>> selectListUser();
}
