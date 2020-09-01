package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.da.ChatDA;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@Log4j2
public class WSHandler {
  private Map<String, Set<ServerWebSocket>> clients;
  private final ChatDA chatDA;
  private final ChatCache chatCache;

  public void addClient(ServerWebSocket webSocket, String userId) {
    if (clients.containsKey(userId)) {
      clients.get(userId).add(webSocket);
    } else {
      Set<ServerWebSocket> client = new ConcurrentHashSet<>();
      client.add(webSocket);
      clients.put(userId, client);
    }
  }

  public void removeClient(ServerWebSocket webSocket, String userId) {
    if (clients.containsKey(userId)) {
      Set<ServerWebSocket> removedClient = clients.get(userId);
      removedClient.remove(webSocket);
      if (removedClient.isEmpty()) clients.remove(userId);
    }
  }

  public void handle(Buffer buffer, String userId) {
    log.info("Hanlde websocket");
    JsonObject json = new JsonObject(buffer.toString());
//    log.info("Buffer: {}",buffer.toString());
    String type = json.getString("type");
    switch (type) {
      case "SEND":
        {
          WsMessage message = JsonProtoUtils.parseGson(buffer.toString(), WsMessage.class);
          log.info("Message: {}",message.getMsg());
          log.info("ReceiverId: {}",message.getReceiver_id());
          message.builder()
              .sender_id(userId) // receiver_id existed
              .create_date(new Date())
              .build();
          handleSendMessage(message.toBuilder().type("FETCH").build(), userId);
          handleSendMessage(message, message.getReceiver_id());
          log.info(">> Send messages done");
        }

      case "FETCH":
      default:
    }
  }

  private void handleSendMessage(WsMessage message, String userId) {
//      Store message to db and cache.
    chatDA.insertMsg(message);
    chatCache.set(message);
    Set<ServerWebSocket> receiverCon = clients.get(userId);
    receiverCon.forEach(
        conn -> {
          conn.writeTextMessage(JsonProtoUtils.printGson(message));

        });
  }
}
