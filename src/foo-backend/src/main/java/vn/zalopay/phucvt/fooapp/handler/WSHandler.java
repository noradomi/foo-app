package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.da.ChatDA;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

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
    WsMessage message = JsonProtoUtils.parseGson(buffer.toString(), WsMessage.class);
    message.setSenderId(userId);
    message.setCreateTime(Instant.now().getEpochSecond());
    message.setId(GenerationUtils.generateId());
    chatDA
        .insert(message)
        .setHandler(
            event -> {
              if (event.succeeded()) {
                handleSucceedInsertDB(userId, message);
              } else {
                log.warn("add message to db failed", event.cause());
              }
            });
  }

  private void handleSucceedInsertDB(String userId, WsMessage message) {
    chatCache
        .addToList(message)
        .setHandler(
            asyncResult -> {
              if (asyncResult.succeeded()) {
                handleSendMessage(message.toBuilder().type("FETCH").build(), userId);
                handleSendMessage(message, message.getReceiverId());
              } else {
                log.warn("add message to cache failed", asyncResult.cause());
              }
            });
  }

  private void handleSendMessage(WsMessage message, String receiverId) {
    if (clients.containsKey(receiverId)) {
      Set<ServerWebSocket> receiverCon = clients.get(receiverId);
      receiverCon.forEach(
          conn -> {
            conn.writeTextMessage(JsonProtoUtils.printGson(message));
          });
    }
  }

  public void notifyStatusUserChange(WsMessage notifyMessage) {
    for (Set<ServerWebSocket> client : clients.values()) {
      client.forEach(
          conn -> {
            conn.writeTextMessage(JsonProtoUtils.printGson(notifyMessage));
          });
    }
  }

  public Set<String> getOnlineUserIds() {
    return clients.keySet();
  }
}
