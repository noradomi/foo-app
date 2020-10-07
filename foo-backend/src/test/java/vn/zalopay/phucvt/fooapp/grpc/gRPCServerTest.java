package vn.zalopay.phucvt.fooapp.grpc;

import com.proto.greet.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class gRPCServerTest {
  @Rule public final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

  @Test
  public void getBalance_replyMessage() throws IOException {
    String serverName = InProcessServerBuilder.generateName();

    grpcCleanupRule.register(
        InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(new GreetServiceImpl())
            .build()
            .start());

	  GreetServiceGrpc.GreetServiceBlockingStub blockingStub = GreetServiceGrpc.newBlockingStub(
	  		grpcCleanupRule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
	  );

	  GreetResponse response =blockingStub.greet(GreetRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Noradomi").build()).build());

	  assertEquals("Hello Noradomi", response.getResult());
  }
}
