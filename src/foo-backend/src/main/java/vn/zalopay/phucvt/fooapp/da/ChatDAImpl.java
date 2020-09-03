package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Builder
@Log4j2
public class ChatDAImpl extends BaseTransactionDA implements ChatDA {
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;

  private static final String INSERT_MESSAGE_STATEMENT =
      "INSERT INTO messages (`id`, `sender_id`, `receiver_id`,`message`,`create_date`) VALUES (?,?,?,?,?)";

    private static final String GET_MESSAGE_LIST_STATEMENT =
            "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR ( receiver_id = ? AND sender_id = ?)" +
                    "ORDER BY create_date DESC LIMIT ?, 20";

  @Override
  public Executable<WsMessage> insertMsg(WsMessage msg) {
      log.info("> Insert message to DB");
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

    @Override
    public Future<List<WsMessage>> getMessageList(String firstUserId, String secondUserId, int offset) {
        log.info("> Get message list from DB");
        Future<List<WsMessage>> future = Future.future();
        asyncHandler.run(
                () -> {
                    Object[] params = {firstUserId,secondUserId,firstUserId,secondUserId,offset};
                    queryEntity(
                            "queryListMessage",
                            future,
                            GET_MESSAGE_LIST_STATEMENT,
                            params,
                            this::mapRs2EntityListMessage,
                            dataSource::getConnection,
                            false);
                });
        return future;
    }

    private List<WsMessage> mapRs2EntityListMessage(ResultSet resultSet) throws Exception {
        WsMessage wsMessage = null;
        List<WsMessage> messageList = new ArrayList<>();
        while (resultSet.next()) {
            wsMessage = new WsMessage();
            EntityMapper.getInstance().loadResultSetIntoObject(resultSet, wsMessage);
            messageList.add(wsMessage);
        }

        return messageList;
    }

}
