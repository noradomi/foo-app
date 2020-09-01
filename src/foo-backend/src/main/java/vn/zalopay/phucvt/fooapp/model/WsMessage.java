package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WsMessage {
    private String id;
    private String type;
    private String sender_id;
    private String receiver_id;
    private String msg;
    private long create_date;
}
