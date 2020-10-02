package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryItem {
    @DBTable(columnName = "user_id")
    String userId;

    @DBTable(columnName = "amount")
    long amount;

    @DBTable(columnName = "description")
    String description;

    @DBTable(columnName = "recorded_time")
    long recordedTime;

    @DBTable(columnName = "transfer_type")
    int transferType;
}
