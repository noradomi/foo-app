package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer implements Serializable {
  @DBTable(columnName = "id")
  String transferId;

  @DBTable(columnName = "sender")
  String sender;

  @DBTable(columnName = "receiver")
  String receiver;

  @DBTable(columnName = "amount")
  long amount;

  @DBTable(columnName = "description")
  String description;

  @DBTable(columnName = "recorded_time")
  long recordedTime;
}
