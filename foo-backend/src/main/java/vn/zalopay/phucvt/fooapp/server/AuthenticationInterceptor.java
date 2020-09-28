package vn.zalopay.phucvt.fooapp.server;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class AuthenticationInterceptor implements ServerInterceptor {
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      final ServerCall<ReqT, RespT> serverCall,
      final Metadata metadata,
      final ServerCallHandler<ReqT, RespT> serverCallHandler) {

    final String auth_token =
        metadata.get(Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER));

    System.out.println(metadata);
    System.out.println("JWT = " + auth_token);

    return serverCallHandler.startCall(serverCall, metadata);
  }
}
