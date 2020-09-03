package vn.zalopay.phucvt.fooapp.handler;

import com.google.common.collect.Sets;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.ParticipantDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.ConversationIdResponse;
import vn.zalopay.phucvt.fooapp.utils.JWTUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Log4j2
public class ParticipantHandler extends BaseHandler {

    private final ParticipantDA participantDA;

    private final JWTUtils jwtUtils;

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
        Future<BaseResponse> future = Future.future();

        String paramUserId = baseRequest.getParams().get("userId");

        jwtUtils.getUserIdFromToken(baseRequest)
                .compose(userId -> {
                    List<Future> getConvIdsByUserIdFutures = new ArrayList<>();
                    getConvIdsByUserIdFutures.add(participantDA.getConversationIdByUserId(userId));
                    getConvIdsByUserIdFutures.add(participantDA.getConversationIdByUserId(paramUserId));

                    CompositeFuture cp = CompositeFuture.all(getConvIdsByUserIdFutures);
                    cp.setHandler(ar -> {
                        if(ar.succeeded()){
                            ConversationIdResponse dataObj = new ConversationIdResponse();
                            Set<String> conversation_ids = Sets.intersection(cp.resultAt(0),cp.resultAt(1));
                            if(!conversation_ids.isEmpty())
                            {
                                conversation_ids.forEach(id -> dataObj.setConversation_id(id));
                            }
                            else{
                                dataObj.setConversation_id("-1");
                            }

                            SuccessResponse successResponse = SuccessResponse
                                    .builder()
                                    .data(dataObj)
                                    .build();
                            successResponse.setStatus(HttpResponseStatus.OK.code());
                            future.complete(successResponse);
                        }
                        else {
                            future.fail(ar.cause());
                        }
                    });
                },Future.future().setHandler(handler -> future.fail(handler.cause())));

        return future;
    }
}
