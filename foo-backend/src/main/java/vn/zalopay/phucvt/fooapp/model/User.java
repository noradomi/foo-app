package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
  @DBTable(columnName = "id")
  String userId;

  @DBTable(columnName = "username")
  String username;

  @DBTable(columnName = "password")
  String password;

  @DBTable(columnName = "name")
  String name;

  boolean online = false;
}
