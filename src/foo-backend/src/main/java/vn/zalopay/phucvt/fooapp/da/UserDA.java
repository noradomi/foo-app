package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.model.User;
import io.vertx.core.Future;

public interface UserDA {
  Executable<User> insert(User user);

  Future<User> selectUserById(String id);

  Future<User> selectUserByUserName(String username);


}
