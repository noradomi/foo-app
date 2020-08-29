package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationListItem {
    private String name;
    private String conversationId;
    private String lastMessage;
}
