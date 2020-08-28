package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.entity.request.MessageRequest;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WSHandler {
    private Map<String, ServerWebSocket> clients = new ConcurrentHashMap<>();

    public void addClient(ServerWebSocket webSocket) {
        String username = getUsername(webSocket.headers());
        clients.put(username, webSocket);
    }

    private String getUsername(MultiMap headers) {
        return headers.get("username");
    }

    public void removeClient(ServerWebSocket webSocket) {
        String username = getUsername(webSocket.headers());
        clients.remove(username);
    }

    public void handle(Buffer buffer) {
        JsonObject json = new JsonObject(buffer.toString());
        String type = json.getString("type");
        switch (type) {
            case "SEND":
                handleSendMessage(buffer);
            case "FETCH":
            default:
        }
    }

    private void handleSendMessage(Buffer buffer) {
        MessageRequest messageRequest =
                JsonProtoUtils.parseGson(buffer.toString(), MessageRequest.class);
        ServerWebSocket serverWebSocket = clients.get(messageRequest.getReceiver());
        serverWebSocket.write(buffer);
    }
}
