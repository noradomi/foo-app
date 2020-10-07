package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryItem {
  @DBTable(columnName = "sender_id")
  String senderId;

  @DBTable(columnName = "receiver_id")
  String receiverId;

  @DBTable(columnName = "amount")
  long amount;

  @DBTable(columnName = "description")
  String description;

  @DBTable(columnName = "recorded_time")
  long recordedTime;

  @DBTable(columnName = "transfer_type")
  int transferType;
}
