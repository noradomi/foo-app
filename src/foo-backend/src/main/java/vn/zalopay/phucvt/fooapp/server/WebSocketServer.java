package vn.zalopay.phucvt.fooapp.server;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

@Builder
@Log4j2
public class WebSocketServer {
  private final WSHandler wsHandler;
  private final Vertx vertx;
  private final int port;
  private HttpServer listen;
  private final JWTUtils jwtUtils;

  private Future<String> authenticated(ServerWebSocket ws) {
    Future<String> future = Future.future();

    String query = ws.query();
    if (!StringUtils.isBlank(query)) {
      String token = query.substring(query.indexOf('=') + 1);
      if (!StringUtils.isBlank(token)) {
        jwtUtils.authenticate(token).setHandler(userIdRes ->{
          if(userIdRes.succeeded())
            future.complete(userIdRes.result());
        });
      } else future.fail("Token is null");
    } else future.fail("Missing jwt param");
    return future;
  }

  public void start() {
    log.info("Starting WebSocket server at port {}",port);
    HttpServer listen =
        vertx
            .createHttpServer()
            .websocketHandler(
                ws -> {
//                  log.info(ws.headers());
//                  String query = ws.query();
//                  log.info(query.substring(query.indexOf('=') + 1));
                  authenticated(ws)
//                          .compose(userId -> {
//                            log.info("userId : {}",userId);
//                          },Future.failedFuture("Failed"));
                      .setHandler(
                          userIdAsynRes -> {
                            if (userIdAsynRes.succeeded()) {
                              String userId = userIdAsynRes.result();
                              log.info("userId : {}",userId);
//                              if (!ws.path().equals("/chat")) {
//                                log.info("Path failed");
//                                ws.reject();
//                              } else {

                                ws.accept();
                                wsHandler.addClient(ws, userId);
                                ws.closeHandler(event -> wsHandler.removeClient(ws, userId));
                                ws.handler(buffer -> wsHandler.handle(buffer, userId));

                            } else {
                              log.info("Authentication faield");
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
