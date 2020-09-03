package vn.zalopay.phucvt.fooapp.server;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

@Builder
@Log4j2
public class WebSocketServer {
  private final WSHandler wsHandler;
  private final Vertx vertx;
  private final int port;
  private HttpServer listen;
  private final JWTUtils jwtUtils;
  private final UserCache userCache;

  private Future<String> authenticated(ServerWebSocket ws) {
    Future<String> future = Future.future();

    String query = ws.query();
    if (!StringUtils.isBlank(query)) {
      String token = query.substring(query.indexOf('=') + 1);
      if (!StringUtils.isBlank(token)) {
        jwtUtils
            .authenticate(token)
            .setHandler(
                userIdRes -> {
                  if (userIdRes.succeeded()) future.complete(userIdRes.result());
                });
      } else future.fail("Token is null");
    } else future.fail("Missing jwt param");
    return future;
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
                          userIdAsynRes -> {
                            if (userIdAsynRes.succeeded()) {
                              String userId = userIdAsynRes.result();
                              ws.accept();

                              log.info("Connected with a user: {}", userId);
                              //     Remove user from cache online status
                              userCache.setOnlineUserStatus(userId);

                              wsHandler.addClient(ws, userId);

                              //    Notify to others client to update user
                              // list.
                              wsHandler.notifyStatusUserChange(WsMessage.builder().type("ONLINE").sender_id(userId).build());

                              ws.closeHandler(
                                  event -> {
                                    log.info(
                                        "Web Socket : Close connections with userId:{}", userId);
                                    //    Remove user from cache online status
                                    userCache.delOnlineUserStatus(userId);
                                    wsHandler.removeClient(ws, userId);
                                    wsHandler.notifyStatusUserChange(WsMessage.builder().type("OFFLINE").sender_id(userId).build());
                                  });
                              ws.handler(buffer -> wsHandler.handle(buffer, userId));

                            } else {
                              log.error("Web Socket: Authenticate JWT failed !");
                              ws.reject();
                            }
                          });
                })
            .listen(port);
  }

  public void close() {
    listen.close();
  }
}
