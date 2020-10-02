package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.AccountLog;
import vn.zalopay.phucvt.fooapp.model.Transfer;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Log4j2
public class FintechDAImpl extends BaseTransactionDA implements FintechDA {
  private static final String SELECT_USER_FOR_UPDATE =
      "SELECT * FROM users WHERE id = ? FOR UPDATE";
  private static final String UPDATE_USER_BALANCE_STATEMENT =
      "UPDATE users SET balance = ?, last_updated = ? WHERE id = ?";
  private static final String INSERT_TO_TRANSFER_STATEMENT =
      "INSERT INTO transfers (`id`, `sender`, `receiver`,`amount`,`recorded_time`,`description`) VALUES (?,?,?,?,?,?)";
  private static final String INSERT_TO_ACCOUNT_LOG_STATEMENT =
      "INSERT INTO account_logs (`id`, `user_id`, `transfer_id`,`balance`,`transfer_type`,recorded_time) VALUES (?,?,?,?,?,?)";
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  @Override
  public Future<User> selectUserForUpdate(String id) {
    Future<User> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {id};
          queryEntity(
              "selectUserForUpdate",
              future,
              SELECT_USER_FOR_UPDATE,
              params,
              this::mapRs2EntityUser,
              dataSource::getConnection,
              false);
        });
    return future;
  }

  @Override
  public Executable<Void> updateBalance(String userId, long newBalance, long lastUpdated) {
    return connection -> {
      Future<Void> future = Future.future();
      asyncHandler.run(
          () -> {
            Object[] params = {newBalance, lastUpdated, userId};
            try {
              executeWithParams(
                  future,
                  connection.unwrap(),
                  UPDATE_USER_BALANCE_STATEMENT,
                  params,
                  "updateBalance");
            } catch (SQLException e) {
              future.fail(e);
            }
          });

      return future;
    };
  }

  @Override
  public Executable<Void> insertTransferLog(Transfer transfer) {
    return connection -> {
      Future<Void> future = Future.future();
      asyncHandler.run(
          () -> {
            Object[] params = {
              transfer.getTransferId(),
              transfer.getSender(),
              transfer.getReceiver(),
              transfer.getAmount(),
              transfer.getRecordedTime(),
              transfer.getDescription()
            };
            try {
              executeWithParams(
                  future,
                  connection.unwrap(),
                  INSERT_TO_TRANSFER_STATEMENT,
                  params,
                  "insertTransfer");
            } catch (SQLException e) {
              future.fail(e);
            }
          });

      return future;
    };
  }

  @Override
  public Executable<Void> insertAccountLog(AccountLog accountLog) {
    return connection -> {
      Future<Void> future = Future.future();
      asyncHandler.run(
          () -> {
            Object[] params = {
              accountLog.getId(),
              accountLog.getUserId(),
              accountLog.getTransferId(),
              accountLog.getBalance(),
              accountLog.getTransferType(),
              accountLog.getRecordedTime()
            };
            try {
              executeWithParams(
                  future,
                  connection.unwrap(),
                  INSERT_TO_ACCOUNT_LOG_STATEMENT,
                  params,
                  "insertAccountLog");
            } catch (SQLException e) {
              future.fail(e);
            }
          });

      return future;
    };
  }

  private User mapRs2EntityUser(ResultSet resultSet) throws Exception {
    User user = null;
    while (resultSet.next()) {
      user = new User();
      EntityMapper.getInstance().loadResultSetIntoObject(resultSet, user);
    }
    return user;
  }
}
