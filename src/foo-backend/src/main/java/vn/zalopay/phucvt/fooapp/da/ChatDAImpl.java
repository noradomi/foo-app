package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.SQLException;

@Builder
@Log4j2
public class ChatDAImpl extends BaseTransactionDA implements ChatDA {
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  private static final String INSERT_MESSAGE_STATEMENT =
      "INSERT INTO messages (`id`, `sender_id`, `receiver_id`,`message`,`create_date`) VALUES (?,?,?,?,?)";

  @Override
  public Executable<WsMessage> insertMsg(WsMessage msg) {
      log.info("insert message");
      return connection -> {
          Future<Void> future = Future.future();
          asyncHandler.run(
                  () -> {
                      Object[] params = {
                              msg.getId(),
                              msg.getSender_id(),
                              msg.getReceiver_id(),
                              msg.getMsg(),
                              msg.getCreate_date()
                      };
                      try {
                          executeWithParams(
                                  future, connection.unwrap(), INSERT_MESSAGE_STATEMENT, params, "insertMessage");
                          log.info("insert message done");
                      } catch (SQLException e) {
                          log.info("insert user fail caused by {}", e.getMessage());
                          future.fail(e);
                      }
                  });

          return Future.succeededFuture(msg);
      };
  }

}
