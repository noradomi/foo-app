package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;

public interface BlackListCache {
    Future<String> add(String userId);

    Future<Boolean> contains(String userId);
}
