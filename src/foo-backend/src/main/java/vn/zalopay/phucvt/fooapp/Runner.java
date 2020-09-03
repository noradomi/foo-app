package vn.zalopay.phucvt.fooapp;

import vn.zalopay.phucvt.fooapp.config.FileConfigLoader;
import vn.zalopay.phucvt.fooapp.config.ServiceConfig;
import vn.zalopay.phucvt.fooapp.dagger.DaggerServiceComponent;
import vn.zalopay.phucvt.fooapp.dagger.ServiceComponent;
import vn.zalopay.phucvt.fooapp.dagger.ServiceModule;
import vn.zalopay.phucvt.fooapp.server.RestfulAPI;
import vn.zalopay.phucvt.fooapp.server.WebSocketServer;
//import vn.zalopay.phucvt.fooapp.utils.Tracker;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;


public class Runner {

    public static void main(String[] args) {
        try {
            System.out.println("NORADOMI CHAT");
            ServiceConfig serviceConfig =
                    FileConfigLoader.loadFromEnv("development.yaml", ServiceConfig.class);

//            Tracker.initialize("example");

            ServiceModule module = ServiceModule.builder().serviceConfig(serviceConfig).build();
            ServiceComponent component = DaggerServiceComponent.builder().serviceModule(module).build();

            VertxOptions vertxOptions = new VertxOptions();
            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setInstances(vertxOptions.getEventLoopPoolSize());
            RestfulAPI restfulAPI = component.getRestfulAPI();
            WebSocketServer webSocketServer = component.getWebSocketServer();

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
        }
        catch (Exception e) {
            System.exit(1);
        }

    }
}
