package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.WsMessage;

import java.sql.Connection;
import java.util.List;

public interface ChatDA {
  Future<Void> insert(WsMessage msg);

  Future<Void> insertWithConnParam(WsMessage msg, Connection connection);

  Future<List<WsMessage>> getMessageList(String firstUserId, String secondUserId, int offset);
}
