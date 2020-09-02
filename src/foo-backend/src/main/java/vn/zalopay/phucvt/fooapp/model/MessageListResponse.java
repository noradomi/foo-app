package vn.zalopay.phucvt.fooapp.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageListResponse {
    List<WsMessage> items = new ArrayList<>();
    int currentOffset;

}
