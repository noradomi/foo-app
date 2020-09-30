package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.*;
import io.jsonwebtoken.*;
import lombok.Builder;

import java.nio.charset.StandardCharsets;

@Builder
public class AuthInterceptor implements ServerInterceptor {
  public static final Context.Key<String> USER_ID = Context.key("userId");
  private final String  publicKey;
  private JwtParser parser;

  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      final ServerCall<ReqT, RespT> serverCall,
      final Metadata metadata,
      final ServerCallHandler<ReqT, RespT> serverCallHandler) {

    parser = Jwts.parser().setSigningKey(publicKey.getBytes(StandardCharsets.UTF_8));
    Status status = Status.OK;
    final String jwt = metadata.get(Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER));
    Jws<Claims> claims = null;
    try {
      claims = parser.parseClaimsJws(jwt);
    } catch (JwtException e) {
      e.printStackTrace();
      status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
    }
    if (claims != null) {
      String userId = claims.getBody().get("userId").toString();
      Context context = Context.current().withValue(USER_ID, userId);
      return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }
    serverCall.close(status, new Metadata());
    return new ServerCall.Listener<ReqT>() {
      // noop
    };
  }
}
