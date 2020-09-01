package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

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
    private Date create_date;
}
