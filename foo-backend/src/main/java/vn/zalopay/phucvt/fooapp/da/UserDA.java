package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.Friend;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;

import java.util.List;

public interface UserDA {
  Future<Void> insert(User user);

  Future<User> selectUserById(String id);

  Future<User> selectUserByUserName(String username);

  Future<User> selectUserByFullName(String name);

  Future<List<User>> selectListUser();

  Future<Void> addFriend(Friend friend);

  Future<List<UserFriendItem>> getFriendList(String userId);

  Future<List<User>> getStrangerList(String userId);

  Future<Void> resetUnseen(String userId, String friendId);

  void increaseUnseenMessages(String userId, String friendId);

  void updateLastMessage(String message, String userId, String friendId);
}
