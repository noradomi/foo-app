package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.common.mapper.EntityMapper;
import vn.zalopay.phucvt.fooapp.config.ChatConfig;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.AsyncHandler;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Builder
@Log4j2
public class ChatDAImpl extends BaseTransactionDA implements ChatDA {
  private static final String INSERT_MESSAGE_STATEMENT =
      "INSERT INTO messages (`id`, `sender`, `receiver`,`message`,`create_time`, `message_type`) VALUES (?,?,?,?,?,?)";
  private static final String GET_MESSAGE_LIST_STATEMENT =
      "SELECT m.id,m.sender,m.receiver,m.message,m.create_time, m.message_type FROM \n"
          + "(SELECT * FROM messages WHERE (sender = ? AND receiver = ?) \n"
          + "UNION \n"
          + "SELECT * FROM messages WHERE (sender = ? AND receiver = ?)) \n"
          + "as m\n"
          + "ORDER BY create_time DESC LIMIT ?, ?";
  private final DataSource dataSource;
  private final AsyncHandler asyncHandler;
  private final ChatConfig chatConfig;

  @Override
  public Future<Void> insert(WsMessage message) {
    Future<Void> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {
            message.getId(),
            message.getSenderId(),
            message.getReceiverId(),
            message.getMessage(),
            message.getCreateTime(),
            message.getMessageType()
          };
          try {
            executeWithParams(
                future,
                dataSource.getConnection(),
                INSERT_MESSAGE_STATEMENT,
                params,
                "insertMessage");
          } catch (SQLException e) {
            log.error("insert message to db failed caused by {}", ExceptionUtil.getDetail(e));
            future.fail(e);
          }
        });
    return future;
  }

  @Override
  public Future<List<WsMessage>> getMessageList(
      String firstUserId, String secondUserId, int offset) {
    Future<List<WsMessage>> future = Future.future();
    asyncHandler.run(
        () -> {
          Object[] params = {
            firstUserId,
            secondUserId,
            secondUserId,
            firstUserId,
            offset,
            chatConfig.getNumberOfMessagesRetrieve()
          };
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
