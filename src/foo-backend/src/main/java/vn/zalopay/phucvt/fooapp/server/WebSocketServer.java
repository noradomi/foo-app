package vn.zalopay.phucvt.fooapp.server;

import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class WebSocketServer {
    private final WSHandler wsHandler;
    private final Vertx vertx;
    private final int port;
    private HttpServer listen;

    public WebSocketServer(WSHandler wsHandler, Vertx vertx, int port) {
        this.wsHandler = wsHandler;
        this.vertx = vertx;
        this.port = port;
    }

    private boolean authenticated(MultiMap headers) {
        return true;
    }

    public void start() {
        HttpServer listen =
                vertx
                        .createHttpServer()
                        .websocketHandler(
                                ws -> {
                                    if (!authenticated(ws.headers())) {
                                        ws.reject();
                                    }
                                    if (!ws.path().equals("/chat")) {
                                        ws.reject();
                                    } else {

                                        wsHandler.addClient(ws);
                                        ws.closeHandler(event -> wsHandler.removeClient(ws));

                                        ws.handler(buffer -> wsHandler.handle(buffer));
                                    }
                                })
                        .listen(port);
    }

    public void close() {
        listen.close();
    }
}
