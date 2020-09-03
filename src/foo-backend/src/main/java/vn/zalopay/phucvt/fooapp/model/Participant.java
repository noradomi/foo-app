package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @DBTable(columnName = "id")
    private String id;
    @DBTable(columnName = "conversation_id")
    private String conversation_id;
    @DBTable(columnName = "user_id")
    private String user_id;
//    private String type;
}
