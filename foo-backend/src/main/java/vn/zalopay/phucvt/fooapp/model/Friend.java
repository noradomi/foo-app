package vn.zalopay.phucvt.fooapp.model;

import lombok.*;
import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
  @DBTable(columnName = "id")
  String id;

  @DBTable(columnName = "user_id")
  String userId;

  @DBTable(columnName = "friend_id")
  String friendId;

  @DBTable(columnName = "unread_messages")
  int unreadMessages;

  @DBTable(columnName = "last_message")
  String lastMessage;
}
