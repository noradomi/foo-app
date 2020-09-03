package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

import java.sql.Timestamp;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WsMessage {
    @DBTable(columnName = "id")
    private String id;
    private String type;
    @DBTable(columnName = "sender_id")
    private String sender_id;
    @DBTable(columnName = "receiver_id")
    private String receiver_id;
    @DBTable(columnName = "message")
    private String msg;
    @DBTable(columnName = "create_date")
    private long create_date;
}
