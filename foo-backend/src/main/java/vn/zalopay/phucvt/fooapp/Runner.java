package vn.zalopay.phucvt.fooapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.config.FileConfigLoader;
import vn.zalopay.phucvt.fooapp.config.ServiceConfig;
import vn.zalopay.phucvt.fooapp.dagger.DaggerServiceComponent;
import vn.zalopay.phucvt.fooapp.dagger.ServiceComponent;
import vn.zalopay.phucvt.fooapp.dagger.ServiceModule;
import vn.zalopay.phucvt.fooapp.server.RestfulAPI;
import vn.zalopay.phucvt.fooapp.server.WebSocketServer;
import vn.zalopay.phucvt.fooapp.grpc.gRPCServer;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import java.io.IOException;

@Log4j2
public class Runner {

  public static void main(String[] args) {
    try {
      ServiceConfig serviceConfig =
          FileConfigLoader.loadFromEnv("service.conf", ServiceConfig.class);

      ServiceModule module = ServiceModule.builder().serviceConfig(serviceConfig).build();
      ServiceComponent component = DaggerServiceComponent.builder().serviceModule(module).build();

      VertxOptions vertxOptions = new VertxOptions();
      DeploymentOptions deploymentOptions = new DeploymentOptions();
      deploymentOptions.setInstances(vertxOptions.getEventLoopPoolSize());
      RestfulAPI restfulAPI = component.getRestfulAPI();
      WebSocketServer webSocketServer = component.getWebSocketServer();
      gRPCServer gRPCServer = component.getGRPCServer();

      component
          .getVertx()
          .deployVerticle(
              () ->
                  new AbstractVerticle() {

                    @Override
                    public void start() {
                      restfulAPI.start();
                      webSocketServer.start();
                    }

                    @Override
                    public void stop() throws Exception {
                      restfulAPI.close();
                      webSocketServer.close();
                    }
                  },
              new DeploymentOptions().setInstances(8));

      try {
        gRPCServer.start();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      log.error("start Runner failed cause={}", ExceptionUtil.getDetail(e));
      System.exit(1);
    }
  }
}
