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
import vn.zalopay.phucvt.fooapp.model.WsMessage;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

import java.util.ArrayList;
import java.util.List;

@Builder
@Log4j2
public class RecentMessagesHandler extends BaseHandler {
  private final ChatDA chatDA;
  private final ChatCache chatCache;
  private final JWTUtils jwtUtils;

  @Override
  public Future<BaseResponse> handle(BaseRequest baseRequest) {
    Future<BaseResponse> future = Future.future();
    String paramUserId = baseRequest.getParams().get("userId");

    jwtUtils
        .getUserIdFromToken(baseRequest)
            .compose(userId -> {

                Future<List<WsMessage>> recentMsgFuture = chatCache.getList(userId,paramUserId);
                recentMsgFuture.setHandler(res -> {
                    List<WsMessage> msgList = new ArrayList<>();
                    if(res.succeeded()){
                        log.trace("Cache hit");
                        msgList = res.result();
                        SuccessResponse response = SuccessResponse.builder()
                                .data(msgList)
                                .build();
                        response.setStatus(HttpResponseStatus.OK.code());
                        future.complete(response);
                    }
                    else{
                        log.trace("Cache miss");
                        future.fail("Cache miss");
                    }
                });
            },Future.future().setHandler(hanlder -> {
                future.fail(hanlder.cause());
            }));
    return future;
  }
}
