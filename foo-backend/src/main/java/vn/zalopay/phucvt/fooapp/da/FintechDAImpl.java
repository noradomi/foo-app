package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.model.Transfer;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.SQLException;

@Builder
public class FintechDAImpl extends BaseTransactionDA implements FintechDA {
  private static final String UPDATE_USER_BALANCE_STATEMENT =
      "UPDATE users SET balance = balance + ? WHERE id = ?";
  private static final String INSERT_TO_TRANSFER_STATEMENT =
      "INSERT INTO transfer (`id`, `sender`, `receiver`,`amount`,`recorded_time`,`description`) VALUES (?,?,?,?,?,?)";
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  @Override
  public Executable<Void> updateBalance(String userId, long amount) {
    return connection -> {
      Future<Void> future = Future.future();
      asyncHandler.run(
          () -> {
            Object[] params = {userId, amount};
            try {
              executeWithParams(
                  future, connection.unwrap(), UPDATE_USER_BALANCE_STATEMENT, params, "updateBalance");
            } catch (SQLException e) {
              future.fail(e);
            }
          });

      return future;
    };
  }

  @Override
  public Executable<Transfer> insertTransferLog(Transfer transfer) {
    return connection -> {
      Future<Transfer> future = Future.future();
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
                  future, connection.unwrap(), INSERT_TO_TRANSFER_STATEMENT, params, "insertTransfer");
            } catch (SQLException e) {
              future.fail(e);
            }
          });

      return future;
    };
  }
}
