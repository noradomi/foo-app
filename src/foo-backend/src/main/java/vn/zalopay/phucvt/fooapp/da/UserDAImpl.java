package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAImpl extends BaseTransactionDA implements UserDA {
  private static final Logger LOGGER = LogManager.getLogger(UserDAImpl.class);
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  private static final String INSERT_USER_STATEMENT =
      "INSERT INTO users (`id`, `username`, `password`,`fullname`) VALUES (?, ?,?,?)";
  private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
  private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
  private static final String SELECT_LIST_USERS = "SELECT * FROM users";
  private static final String GET_SINGLE_CONVERSATION_ID =
      "SELECT p1.conversation_id FROM PARTICIPANTS p1 WHERE p1.user_id = ? INTERSECT SELECT p2.conversation_id FROM PARTICIPANTS WHERE p2.user_id = ?";

  public UserDAImpl(DataSource dataSource, AsyncHandler asyncHandler) {
    super();
    this.dataSource = dataSource;
    this.asyncHandler = asyncHandler;
  }

  @Override
  public Executable<User> insert(User user) {
    LOGGER.info("> Insert user");
    return connection -> {
      Future<Void> future = Future.future();
      asyncHandler.run(
          () -> {
            if (user.getUserId() == null) {
              LOGGER.info("Id user null");
            }
            Object[] params = {
              user.getUserId(), user.getUsername(), user.getPassword(), user.getFullname()
            };
            try {
              executeWithParams(
                  future, connection.unwrap(), INSERT_USER_STATEMENT, params, "insertUser");
              LOGGER.info("insert user done");
            } catch (SQLException e) {
              LOGGER.info("insert user fail caused by {}", e.getMessage());
              future.fail(e);
            }
          });

      return Future.succeededFuture(user);
    };
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
    LOGGER.info("> Select user by user name");
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
    LOGGER.info("end select");
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
              SELECT_LIST_USERS,
              params,
              this::mapRs2EntityListUser,
              dataSource::getConnection,
              false);
        });

    return future;
  }

  @Override
  public Future<String> getSingleConversationId(String userId1, String userId2) {
    Future<String> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {userId1, userId2};
          queryEntity(
              "querySingleConversationId",
              future,
              GET_SINGLE_CONVERSATION_ID,
              params,
              this::mapRs2StringConversationId,
              dataSource::getConnection,
              false);
        });

    return future;
  }

  private String mapRs2StringConversationId(ResultSet resultSet) throws Exception {
    String res = "";

    while (resultSet.next()) {
      res = resultSet.getString("conversation_id");
    }

    return res;
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
