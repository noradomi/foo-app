package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;

public interface BlackListCache {
  Future<String> set(String token, Long ttl);
  Future<Boolean> contains(String token);
}
