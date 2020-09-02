package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.da.ChatDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.MessageListResponse;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Builder
@Log4j2
public class MessageListHandler extends BaseHandler {
  private final ChatCache chatCache;
  private final ChatDA chatDA;
  private JWTUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    log.info("> FETCH MESSAGE LIST");
    Future<BaseResponse> future = Future.future();

    String friendId = baseRequest.getParams().get("friendId");
    int offset = Integer.parseInt(baseRequest.getParams().get("offset"));

    jwtUtils
        .getUserIdFromToken(baseRequest)
        .setHandler(
            userIdAsyncRes -> {
              if (userIdAsyncRes.succeeded()) {
                String userId = userIdAsyncRes.result();

                if (offset == 0) { // load message list first time.
                  // Load messages cache
                  chatCache
                      .getList(userId, friendId)
                      .setHandler(
                          msgListAsyncRes -> {
                            if (msgListAsyncRes.succeeded()) {
                              log.info("Get messages list: CACHE HIT");
                              List<WsMessage> messageList = msgListAsyncRes.result();
                              messageList.sort(Comparator.comparing(WsMessage::getCreate_date));

                              MessageListResponse data =
                                  MessageListResponse.builder()
                                      .items(messageList)
                                      .currentOffset(offset + messageList.size())
                                      .build();

                              SuccessResponse response =
                                  SuccessResponse.builder().data(data).build();
                              response.setStatus(HttpResponseStatus.OK.code());
                              future.complete(response);
                            } else {
                              log.info("Get messages list: CACHE MISS -> LOAD FROM DB");
                              getMessageListFromDB(future, userId, friendId, offset, true);
                            }
                          });

                } else {
                  log.info("Get messages list: FETCH OLDER MESSAGE FROM DB");
                  getMessageListFromDB(future, userId, friendId, offset, false);
                }
              }
            });

    return future;
  }

  //  Handle get message list from DB when cache miss or get older messages.
  public void getMessageListFromDB(
      Future<BaseResponse> future, String userId, String friendId, int offset, boolean flag) {
    //        flag: true (cache miss), false (fetch older data)
    chatDA
        .getMessageList(userId, friendId, offset)
        .setHandler(
            event -> {
              if (event.succeeded()) {
                List<WsMessage> messageList = event.result();
                Collections.reverse(messageList);
                if (flag) {
                  // add message list to cache (first time)
                  for (WsMessage wsMessage : messageList) {
                    chatCache.set(wsMessage);
                  }
                }
                MessageListResponse data =
                    MessageListResponse.builder()
                        .items(messageList)
                        .currentOffset(offset + messageList.size())
                        .build();

                SuccessResponse response = SuccessResponse.builder().data(data).build();
                response.setStatus(HttpResponseStatus.OK.code());
                future.complete(response);
              } else {
                future.fail("Get message failed");
              }
            });
  }
}
