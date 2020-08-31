package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.WsMessage;

import java.util.List;

public interface ChatCache {
    Future<Void> set(WsMessage msg);

    Future<List<WsMessage>> getList(String firstUserId, String secondUserId);
}
