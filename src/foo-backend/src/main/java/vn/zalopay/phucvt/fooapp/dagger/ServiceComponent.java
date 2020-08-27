package vn.zalopay.phucvt.fooapp.dagger;

import vn.zalopay.phucvt.fooapp.server.RestfulAPI;
import vn.zalopay.phucvt.fooapp.server.WebSocketServer;
import dagger.Component;
import io.vertx.core.Vertx;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
  RestfulAPI getRestfulAPI();

  WebSocketServer getWebSocketServer();

  Vertx getVertx();

}
