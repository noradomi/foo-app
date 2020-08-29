package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.model.User;
import io.vertx.core.Future;

import java.util.List;

public interface UserDA {
  Executable<User> insert(User user);

  Future<User> selectUserById(String id);

  Future<User> selectUserByUserName(String username);

  Future<List<User>> selectListUsersById(String userId);
}
