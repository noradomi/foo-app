package vn.zalopay.phucvt.fooapp.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Builder
@Log4j2
public class gRPCServer {
  private final int port = 5001;
  GreetServiceImpl greetServiceImpl;

  public void start() throws IOException, InterruptedException {
    log.info("gRPC server start successfully !, port {}", port);
    Server server = ServerBuilder.forPort(port).addService(greetServiceImpl).build();
    server.start();
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.out.println("Received Shudown Request");
                  server.shutdown();
                  System.out.println("Successfully stopped the server");
                }));

    server.awaitTermination();
  }
}
