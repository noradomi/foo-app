package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BaseResponse {
  private transient int statusCode; // remove in response

  private String message;

  private Object data;
}
