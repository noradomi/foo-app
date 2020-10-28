package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;

import java.util.List;

public interface UserCache {
  Future<List<User>> setUserList(List<User> users);

  Future<User> addToUserList(User users);

  Future<List<User>> getUserList();

  void setFriendList(List<UserFriendItem> friendList, String userId);

  void appendFriendList(UserFriendItem user, String userId);

  Future<List<UserFriendItem>> getFriendList(String userId);
}
