package vn.zalopay.phucvt.fooapp.server;

import io.grpc.*;

public class AuthenticationInterceptor implements ServerInterceptor {
  public static final Context.Key<String> JWT
          = Context.key("jwt");
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      final ServerCall<ReqT, RespT> serverCall,
      final Metadata metadata,
      final ServerCallHandler<ReqT, RespT> serverCallHandler) {

    final String auth_token =
        metadata.get(Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER));

    Context context = Context.current().withValue(JWT, auth_token);
    return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
  }
}
