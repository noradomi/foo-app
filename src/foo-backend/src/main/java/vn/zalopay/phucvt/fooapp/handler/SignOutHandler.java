package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;

@Log4j2
public class SignOutHandler extends BaseHandler{

    private final BlackListCache blackListCache;

    public SignOutHandler(BlackListCache blackListCache) {
        this.blackListCache = blackListCache;
    }

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
//        Set expired time in cache equals time-to-love (TTL) of JWT
//        Tam thoi dua vao black list
        Future<BaseResponse> future = Future.future();
        log.info("sign out");
        String token = baseRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();

        Future<String> t =  blackListCache.set(token, 18000L);

        log.info("get token: {}",t.result());

        SuccessResponse successResponse = SuccessResponse.builder().build();
        successResponse.setStatus(200);
        future.complete(successResponse);
        return future;
    }
}
