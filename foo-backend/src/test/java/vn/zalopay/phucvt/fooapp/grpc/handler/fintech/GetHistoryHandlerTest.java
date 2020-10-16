package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.fintech.GetHistoryResponse;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetHistoryHandlerTest {
  @InjectMocks GetHistoryHandler getHistoryHandler;
  @Mock private FintechDA fintechDA;
  @Mock private FintechCache fintechCache;

  @Test
  public void buildSuccessResponse() {
    List<HistoryItem> historyItemList = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      HistoryItem historyItem =
          HistoryItem.builder()
              .amount(1000L)
              .description("test")
              .receiverId("123")
              .senderId("123")
              .recordedTime(123)
              .transferType(1)
              .build();
      historyItemList.add(historyItem);
    }

    GetHistoryResponse response = getHistoryHandler.buildSuccessResponse(historyItemList, 20, 0);
    int nextPageToken = response.getData().getNextPageToken();

    assertThat(nextPageToken, Is.is(20));
    assertThat(response.getData().getItemsList().size(), Is.is(20));
  }
}
