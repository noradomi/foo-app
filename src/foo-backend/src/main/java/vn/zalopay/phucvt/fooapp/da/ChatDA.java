package vn.zalopay.phucvt.fooapp.da;


import vn.zalopay.phucvt.fooapp.model.WsMessage;

public interface ChatDA {
    Executable<WsMessage> insertMsg(WsMessage msg);
}
