package vn.zalopay.phucvt.fooapp.entity.request;

import io.vertx.core.MultiMap;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseRequest {
  private MultiMap params;
  private String userIP;
  private String requestPath;
  private String postData;
}
