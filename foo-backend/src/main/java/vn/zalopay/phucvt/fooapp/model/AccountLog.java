package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLog {
  @DBTable(columnName = "id")
  String id;

  @DBTable(columnName = "user_id")
  String userId;

  @DBTable(columnName = "transfer_id")
  String transferId;

  @DBTable(columnName = "balance")
  long balance;

  @DBTable(columnName = "transfer_type")
  int transferType;
}
