package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;

import java.util.Date;



@Builder
@Log4j2
public class SignOutHandler extends BaseHandler{

    private final BlackListCache blackListCache;

    private final UserCache userCache;

    private final JWTAuth authProvider;

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
        Future<BaseResponse> future = Future.future();
        log.info("sign out");
        String token = baseRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();

        authProvider.authenticate(new JsonObject().put("jwt",token),event ->{
            if(event.succeeded()){
                User userAuth = event.result();
                String userId = userAuth.principal().getString("userId");

//                Set ttl of token in blacklist cache equals expire time of JWT token.
                long expireTimeInMilliseconds = event.result().principal().getLong("exp") * 1000;
                long ttl = expireTimeInMilliseconds - new Date().getTime();
                log.info("ttl : {}",ttl);
                if (ttl > 0){
                    blackListCache.set(token, ttl);
                }
//                Remove user from online users cache.
                userCache.del(userId);

                log.info("Sign out user {} with token {}",userId,token);
                SuccessResponse successResponse = SuccessResponse.builder().build();
                successResponse.setStatus(200);
                future.complete(successResponse);
            }
        });

        return future;
    }
}
