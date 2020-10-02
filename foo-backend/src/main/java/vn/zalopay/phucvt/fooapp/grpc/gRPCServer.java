package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Builder
@Log4j2
public class gRPCServer {
  private final int port;
  private final FintechServiceImpl fintechService;
  private final AuthInterceptor authInterceptor;
  private Server server;

  public void start() throws IOException, InterruptedException {
    log.info("gRPC server start successfully !, port {}", port);
    server =
        ServerBuilder.forPort(port).addService(fintechService).intercept(authInterceptor).build();
    server.start();
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.err.println("*** shutting down gRPC server since JVM is shutting down");
                  gRPCServer.this.stop();
                  System.err.println("*** grpc server shut down");
                }));

    gRPCServer.this.blockUntilShutdown();
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
