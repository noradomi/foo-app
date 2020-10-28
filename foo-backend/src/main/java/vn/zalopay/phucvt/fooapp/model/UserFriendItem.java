package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendItem {
  @DBTable(columnName = "id")
  String id;

  @DBTable(columnName = "name")
  String name;

  @DBTable(columnName = "unread_messages")
  int unreadMessages;

  @DBTable(columnName = "last_message")
  String lastMessage;

  boolean online = false;
}
