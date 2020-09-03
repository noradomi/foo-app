package vn.zalopay.phucvt.fooapp.da;


import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.WsMessage;

import java.util.List;

public interface ChatDA {
    Executable<WsMessage> insertMsg(WsMessage msg);
    Future<List<WsMessage>> getMessageList(String firstUserId, String secondUserId, int offset);
}
