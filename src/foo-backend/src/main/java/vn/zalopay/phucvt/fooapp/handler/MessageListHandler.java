package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.da.ChatDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.data.MessagesDataResponse;
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JwtUtils;

import java.util.Collections;
import java.util.List;

@Builder
@Log4j2
public class MessageListHandler extends BaseHandler {
  private final ChatCache chatCache;
  private final ChatDA chatDA;
  private JwtUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    Future<BaseResponse> future = Future.future();

    String friendId = baseRequest.getParams().get("friendId");
    int offset = Integer.parseInt(baseRequest.getParams().get("offset"));
    String userId = baseRequest.getPrincipal().getString("userId");

    if (offset == 0) {
      chatCache
          .getMessageList(userId, friendId)
          .setHandler(
              listAsyncResult -> {
                if (listAsyncResult.succeeded()) {
                  log.info("get messages list: cache hit, {}-{}-{}", userId, friendId, offset);
                  List<WsMessage> messageList = listAsyncResult.result();
                  handleResponse(future, offset, messageList);
                } else {
                  log.info("get messages list: cache miss, {}-{}-{}", userId, friendId, offset);
                  getMessageListFromDB(future, userId, friendId, offset, true);
                }
              });
    } else {
      log.info("get more messages from db, {}-{}-{}", userId, friendId, offset);
      getMessageListFromDB(future, userId, friendId, offset, false);
    }
    return future;
  }

  private void handleResponse(
      Future<BaseResponse> future, int offset, List<WsMessage> messageList) {
    MessagesDataResponse data =
        MessagesDataResponse.builder()
            .items(messageList)
            .currentOffset(offset + messageList.size())
            .build();
    BaseResponse response =
        BaseResponse.builder().statusCode(HttpResponseStatus.OK.code()).data(data).build();
    future.complete(response);
  }

  //  Handle get message list from DB when cache miss or get older messages.
  public void getMessageListFromDB(
      Future<BaseResponse> future, String userId, String friendId, int offset, boolean flag) {
    chatDA
        .getMessageList(userId, friendId, offset)
        .setHandler(
            event -> {
              if (event.succeeded()) {
                List<WsMessage> messageList = event.result();
                Collections.reverse(messageList);
                if (flag) {
                  // add message list to cache (first time)
                  chatCache.addMessageList(messageList, userId, friendId);
                }
                handleResponse(future, offset, messageList);
              } else {
                log.error("get messages from db failed", event.cause());
                future.fail(event.cause());
              }
            });
  }
}
