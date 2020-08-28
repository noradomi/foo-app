package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.constant.Status;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.request.EchoRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.EchoResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import io.vertx.core.Future;

public class EchoHandler extends BaseHandler {

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
        SuccessResponse.SuccessResponseBuilder response = SuccessResponse.builder();
        Status status = Status.SUCCESS;
        try {
            EchoRequest request = JsonProtoUtils.parseGson(baseRequest.getPostData(), EchoRequest.class);
            response.data(EchoResponse.builder().message("echo: " + request.getMessage()).build());
        } catch (Exception e) {
            status = Status.INVALID_ARGUMENT;
        }
        return Future.succeededFuture();
    }
}
