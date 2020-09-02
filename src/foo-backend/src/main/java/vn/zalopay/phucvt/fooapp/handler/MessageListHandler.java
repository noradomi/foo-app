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
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

@Builder
@Log4j2
public class MessageListHandler extends BaseHandler{
    private final ChatCache chatCache;
    private final ChatDA chatDA;
    private JWTUtils jwtUtils;


    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
        Future<BaseResponse> future = Future.future();

        String friendId = baseRequest.getParams().get("friendId");
        int offset = Integer.parseInt(baseRequest.getParams().get("offset"));


        jwtUtils.getUserIdFromToken(baseRequest)
                .setHandler(userIdAsyncRes -> {
                    if(userIdAsyncRes.succeeded()){
                        String userId = userIdAsyncRes.result();

                        log.info("UserId -> {}",userId);
                        log.info("FriendId -> {}", friendId);

                        if(offset == 0){ // load message list first time.
//                            Load messages cache
                            chatCache.getList(userId,friendId)
                                    .setHandler(msgListAsyncRes -> {
                                        if(msgListAsyncRes.succeeded()){
                                            log.info("Get messages list: Cache hit");
                                            SuccessResponse response = SuccessResponse
                                                    .builder()
                                                    .data(msgListAsyncRes.result())
                                                    .build();
                                            response.setStatus(HttpResponseStatus.OK.code());
                                            future.complete(response);
                                        }
                                        else{
                                            log.info("Get messages list: Cache miss");
                                            getMessageListFromDB(userId,friendId,offset,1);
                                        }
                                    });

                        }
                        else{
                            log.info("Get messages list: Fetch older data from DB");
                            getMessageListFromDB(userId,friendId,offset,0);
                        }
                    }
                })

        return null;
    }

    public Future<BaseResponse> getMessageListFromDB(String userId, String friendId, int offset, int flag){
//        flag: 1 (cache miss), 0 (fetch older data)
        Future<BaseResponse> future = Future.future();
        chatDA.
    }
}
