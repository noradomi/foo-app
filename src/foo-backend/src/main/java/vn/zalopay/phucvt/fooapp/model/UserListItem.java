package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListItem {
    private String name;
    private String conversationId;
//    private String lastMessage;
}
