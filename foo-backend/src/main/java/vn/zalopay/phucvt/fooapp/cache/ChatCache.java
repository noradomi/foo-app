package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.WsMessage;

import java.util.List;

public interface ChatCache {
  Future<WsMessage> addToList(WsMessage message);

  Future<List<WsMessage>> addMessageList(
      List<WsMessage> messageList, String firstUserId, String secondUserId);

  Future<List<WsMessage>> getMessageList(String firstUserId, String secondUserId);
}
