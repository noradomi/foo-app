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
      "insert into friends ('id','user_id','friend_id', 'unread_messages','last_message') values (?,?,?,?,?)";

  private static final String MARK_READ =
      "update friends set unread_messages = 0 where user_id = ? and friend_id = ?";

  private static final String SELECT_FRIEND_LIST =
          "select uf.id, uf.name, f.unread_messages,f.last_message from users u join friends f on u.id  = f.user_id "
          + "join users uf on uf.id  = f.friend_id where u.id = ?";
  private static final String GET_STRANGER_LIST = "";
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
                future, dataSource.getConnection(), INSERT_USER_STATEMENT, params, "insertUser");
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
            executeWithParams(future, dataSource.getConnection(), ADD_FRIEND, params, "addFriend");
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
  public Future<List<User>> getStrangerList(String userId) {
    return null;
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
  public Future<Void> markRead(String userId, String markedUserId) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {userId, markedUserId};
          try {
            executeWithParams(future, dataSource.getConnection(), MARK_READ, params, "markRead");
          } catch (SQLException e) {
            log.error(
                "mark read failed {}--{}, caused={}",
                userId,
                markedUserId,
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

    private List<UserFriendItem> mapRs2EntityListUserFriendItem(ResultSet resultSet) throws Exception {
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
