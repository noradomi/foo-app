package vn.zalopay.phucvt.fooapp.entity.response.data;

import lombok.*;
import vn.zalopay.phucvt.fooapp.model.WsMessage;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessagesDataResponse {
  List<WsMessage> items = new ArrayList<>();
  int currentOffset;
}
