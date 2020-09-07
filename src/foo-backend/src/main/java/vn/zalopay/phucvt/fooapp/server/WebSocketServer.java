package vn.zalopay.phucvt.fooapp.server;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JwtUtils;

@Builder
@Log4j2
public class WebSocketServer {
  private final WSHandler wsHandler;
  private final Vertx vertx;
  private final int port;
  private HttpServer listen;
  private final JwtUtils jwtUtils;
  private final UserCache userCache;

  private Future<String> authenticated(ServerWebSocket ws) {
    String query = ws.query();
    String token = ws.query().substring(query.indexOf('=') + 1);
    return jwtUtils.authenticate(token);
  }

  public void start() {
    log.info("Web Socket server start successfully !, port {}", port);
    HttpServer listen =
        vertx
            .createHttpServer()
            .websocketHandler(
                ws -> {
                  authenticated(ws)
                      .setHandler(
                          userIdAsyncRes -> {
                            if (userIdAsyncRes.succeeded()) {
                              String userId = userIdAsyncRes.result();
                              ws.accept();
                              log.info("ws connected with user: {}", userId);
                              wsHandler.addClient(ws, userId);
                              wsHandler.notifyStatusUserChange(
                                  WsMessage.builder().type("ONLINE").senderId(userId).build());
                              ws.closeHandler(
                                  event -> {
                                    handleCloseConnection(ws, userId);
                                  });
                              ws.handler(buffer -> wsHandler.handle(buffer, userId));
                            } else {
                              ws.reject();
                            }
                          });
                })
            .listen(port);
  }

  public void handleCloseConnection(ServerWebSocket ws, String userId) {
    log.info("ws : close connection with userId={}", userId);
    wsHandler.removeClient(ws, userId);
    wsHandler.notifyStatusUserChange(WsMessage.builder().type("OFFLINE").senderId(userId).build());
  }

  public void close() {
    listen.close();
  }
}
