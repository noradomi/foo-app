package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListItem {
    private String fullname;
//    private String conversationId;
    private String userId;
//    private String lastMessage;
}
