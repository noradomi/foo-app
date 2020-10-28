package vn.zalopay.phucvt.fooapp.utils;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FooHttpStatusException extends RuntimeException {
  private final int statusCode;
  private final String code;
  private final String payload;

  public FooHttpStatusException(int statusCode, String code, String payload) {
    super(HttpResponseStatus.valueOf(statusCode).reasonPhrase(), null, false, false);
    this.statusCode = statusCode;
    this.code = code;
    this.payload = payload;
  }
}
