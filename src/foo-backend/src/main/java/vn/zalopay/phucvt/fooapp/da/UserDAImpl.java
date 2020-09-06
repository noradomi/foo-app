package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class UserDAImpl extends BaseTransactionDA implements UserDA {
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  private static final String INSERT_USER_STATEMENT =
      "INSERT INTO users (`id`, `username`, `password`,`name`) VALUES (?, ?,?,?)";
  private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
  private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
  private static final String SELECT_USER_LIST = "SELECT * FROM users";

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
            user.getUserId(), user.getUsername(), user.getPassword(), user.getName()
          };
          try {
            executeWithParams(
                future, dataSource.getConnection(), INSERT_USER_STATEMENT, params, "insertUser");
          } catch (SQLException e) {
            log.error("insert user to db fail caused={}", ExceptionUtil.getDetail(e));
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
