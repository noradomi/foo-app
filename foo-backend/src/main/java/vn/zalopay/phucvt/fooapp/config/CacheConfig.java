package vn.zalopay.phucvt.fooapp.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheConfig {
  int maxMessagesSize = 20;
  int expireMessages = 10; // minus
  int expireUserList = 10; // minus
}
