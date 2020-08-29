package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import io.vertx.ext.auth.jwt.JWTAuth;
import lombok.Builder;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;

@Builder
public class UserListHandler extends BaseHandler{

    private final UserDA userDA;

    private final JWTAuth authProvider;

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {



        return null;
    }
}
