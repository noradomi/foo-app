package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.Friend;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.UserFriendItem;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class UserDAImpl extends BaseTransactionDA implements UserDA {
  private static final String INSERT_USER_STATEMENT =
      "INSERT INTO users (`id`, `username`, `password`,`name`,`balance`,`last_updated`) VALUES (?, ?,?,?,?,?)";

  private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

  private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";

  private static final String SELECT_USER_BY_FULLNAME = "SELECT * FROM users WHERE name = ?";

  private static final String SELECT_USER_LIST = "SELECT * FROM users";

  private static final String ADD_FRIEND =
      "insert into friends (`id`,`user_id`,`friend_id`, `unread_messages`,`last_message`) values (?,?,?,?,?)";

  private static final String RESET_UNSEEN =
      "update friends set unread_messages = 0 where user_id = ? and friend_id = ?";

  private static final String INCREASE_UNSEEN_MESSAGES =
      "update friends set unread_messages = unread_messages + 1 where user_id = ? and friend_id = ?";

  private static final String SELECT_FRIEND_LIST =
      "select uf.id, uf.name, f.unread_messages,f.last_message from users u join friends f on u.id  = f.user_id "
          + "join users uf on uf.id  = f.friend_id where u.id = ?";

  private static final String UPDATE_LAST_MESSAGE =
      "update friends set last_message = ? where user_id = ? and friend_id = ?";

  private static final String UPDATE_LAST_MESSAGE_AND_UNSEEN_MESSAGES =
      "update friends set last_message = ?, unread_messages = unread_messages + 1 where user_id = ? and friend_id = ?";

  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  public UserDAImpl(DataSource dataSource, AsyncHandler asyncHandler) {
    super();
    this.dataSource = dataSource;
    this.asyncHandler = asyncHandler;
  }

  @Override
  public Future<Void> insert(User user) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {
            user.getUserId(),
            user.getUsername(),
            user.getPassword(),
            user.getName(),
            user.getBalance(),
            user.getLastUpdated()
          };
          try {
            executeWithParams(
                future,
                dataSource.getConnection(),
                INSERT_USER_STATEMENT,
                params,
                "insertUser",
                false);
          } catch (SQLException e) {
            log.error(
                "insert user={} to db fail caused={}",
                user.getUsername(),
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<Void> addFriend(Friend friend) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {
            friend.getId(),
            friend.getUserId(),
            friend.getFriendId(),
            friend.getUnreadMessages(),
            friend.getLastMessage()
          };
          try {
            executeWithParams(
                future, dataSource.getConnection(), ADD_FRIEND, params, "addFriend", false);
          } catch (SQLException e) {
            log.error(
                "add friend failed {}--{}, caused={}",
                friend.getUserId(),
                friend.getFriendId(),
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<List<UserFriendItem>> getFriendList(String userId) {
    Future<List<UserFriendItem>> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {userId};
          queryEntity(
              "queryListFriend",
              future,
              SELECT_FRIEND_LIST,
              params,
              this::mapRs2EntityListUserFriendItem,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  @Override
  public Future<Void> resetUnseen(String userId, String friendId) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {userId, friendId};
          try {
            executeWithParams(
                future, dataSource.getConnection(), RESET_UNSEEN, params, "resetUnseen", false);
          } catch (SQLException e) {
            log.error(
                "reset unseen failed {}--{}, caused={}",
                userId,
                friendId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<Void> increaseUnseenMessages(String userId, String friendId) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {userId, friendId};
          try {
            executeWithParams(
                future,
                dataSource.getConnection(),
                INCREASE_UNSEEN_MESSAGES,
                params,
                "increaseUnseenMessages",
                false);
          } catch (SQLException e) {
            log.error(
                "increase unseen messages failed {}--{}, caused={}",
                userId,
                friendId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<Void> updateLastMessage(
      String message, String userId, String friendId, Connection connection) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {message, userId, friendId};
          try {
            executeWithParams(
                future,
                connection != null ? connection : dataSource.getConnection(),
                UPDATE_LAST_MESSAGE,
                params,
                "updateLastMessage",
                connection != null);
          } catch (SQLException e) {
            log.error(
                "update last messages failed {}--{}, caused={}",
                userId,
                friendId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<Void> updateLastMessageAndUnseenMessages(
      String message, String userId, String friendId, Connection connection) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {message, userId, friendId};
          try {
            executeWithParams(
                future,
                connection != null ? connection : dataSource.getConnection(),
                UPDATE_LAST_MESSAGE_AND_UNSEEN_MESSAGES,
                params,
                "updateLastMessageAndUnseensMessages",
                connection != null);
          } catch (SQLException e) {
            log.error(
                "updateLastMessageAndUnseenMessages failed {}--{}, caused={}",
                userId,
                friendId,
                ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<User> selectUserById(String id) {
    Future<User> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {id};
          queryEntity(
              "queryUser",
              future,
              SELECT_USER_BY_ID,
              params,
              this::mapRs2EntityUser,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  @Override
  public Future<User> selectUserByUserName(String username) {
    Future<User> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {username};
          queryEntity(
              "queryUserByUserName",
              future,
              SELECT_USER_BY_USERNAME,
              params,
              this::mapRs2EntityUser,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  @Override
  public Future<User> selectUserByFullName(String name) {
    Future<User> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {name};
          queryEntity(
              "queryUserByFullName",
              future,
              SELECT_USER_BY_FULLNAME,
              params,
              this::mapRs2EntityUser,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  @Override
  public Future<List<User>> selectListUser() {
    Future<List<User>> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {};
          queryEntity(
              "queryListUser",
              future,
              SELECT_USER_LIST,
              params,
              this::mapRs2EntityListUser,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  private UserFriendItem mapRs2EntityUserFrinedItem(ResultSet resultSet) throws Exception {
    UserFriendItem user = null;
    while (resultSet.next()) {
      user = new UserFriendItem();
      EntityMapper.getInstance().loadResultSetIntoObject(resultSet, user);
    }
    return user;
  }

  private List<UserFriendItem> mapRs2EntityListUserFriendItem(ResultSet resultSet)
      throws Exception {
    UserFriendItem user = null;
    List<UserFriendItem> listUser = new ArrayList<>();
    while (resultSet.next()) {
      user = new UserFriendItem();
      EntityMapper.getInstance().loadResultSetIntoObject(resultSet, user);
      listUser.add(user);
    }
    return listUser;
  }

  private User mapRs2EntityUser(ResultSet resultSet) throws Exception {
    User user = null;
    while (resultSet.next()) {
      user = new User();
      EntityMapper.getInstance().loadResultSetIntoObject(resultSet, user);
    }
    return user;
  }

  private List<User> mapRs2EntityListUser(ResultSet resultSet) throws Exception {
    User user = null;
    List<User> listUser = new ArrayList<>();
    while (resultSet.next()) {
      user = new User();
      EntityMapper.getInstance().loadResultSetIntoObject(resultSet, user);
      listUser.add(user);
    }
    return listUser;
  }
}
