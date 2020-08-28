package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;

@Log4j2
public class SignOutHandler extends BaseHandler{

    private final UserCache userCache;

    public SignOutHandler(UserCache userCache) {
        this.userCache = userCache;
    }

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {

        return null;
    }
}
