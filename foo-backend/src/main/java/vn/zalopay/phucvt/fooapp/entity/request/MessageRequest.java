package vn.zalopay.phucvt.fooapp.entity.request;

import lombok.Getter;

@Getter
public class MessageRequest {
  private String sender;
  private String receiver;
  private String message;
}
