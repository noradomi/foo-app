package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public abstract class BaseHandler {
  private static final Logger LOGGER =
      LogManager.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());

  public void handle(RoutingContext rc) {
    HttpServerRequest request = rc.request();
    HttpServerResponse response = rc.response();
    String requestPath = request.path();

    BaseRequest baseRequest =
        BaseRequest.builder()
            .requestPath(requestPath)
            .postData(rc.getBodyAsString())
            .params(request.params())
            .headers(request.headers())
            .build();

    handle(baseRequest)
        .setHandler(
            rs -> {
              if (rs.succeeded()) {
                response
                    .setStatusCode(rs.result().getStatus())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonProtoUtils.printGson(rs.result()));
              } else {
                LOGGER.error(
                    "Handle request exception request={}",
                    JsonProtoUtils.printGson(baseRequest),
                    ExceptionUtil.getDetail(rs.cause()));
                response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
              }
            });
  }

  public abstract Future<BaseResponse> handle(BaseRequest baseRequest);
}
