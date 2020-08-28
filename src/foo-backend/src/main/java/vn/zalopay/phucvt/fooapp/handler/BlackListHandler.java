package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.HttpPostMultipartRequestDecoder;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.BlackListCache;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.ExceptionResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.utils.ErrorCode;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

@Log4j2
public class BlackListHandler {
    private final BlackListCache blackListCache;

    public BlackListHandler(BlackListCache blackListCache) {
        this.blackListCache = blackListCache;
    }

    public void handle(RoutingContext rc) {

        HttpServerRequest request = rc.request();
        HttpServerResponse response = rc.response();

        String token = request.headers().get(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
        Future<Boolean> checkExistInBlackList = blackListCache.constains(token);

        checkExistInBlackList.compose(res -> {
            if (res) {
                response
                        .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end("Unauthorized");
            }
            else{
                rc.next();
            }
        },Future.future().setHandler(handler -> {
            response.end();
        }));

    }
}
