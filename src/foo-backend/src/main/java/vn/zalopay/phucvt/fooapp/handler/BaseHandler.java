package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

import java.lang.invoke.MethodHandles;

@Log4j2
public abstract class BaseHandler {
  private static final Logger LOGGER =
      LogManager.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());

  public void handle(RoutingContext routingContext) {
    HttpServerRequest request = routingContext.request();
    HttpServerResponse response = routingContext.response();
    String requestPath = request.path();

    //    User credentials
    User user = routingContext.user();
    JsonObject principal = new JsonObject();
    if (user != null) {
      principal = user.principal();
    }

    BaseRequest baseRequest =
        BaseRequest.builder()
            .requestPath(requestPath)
            .postData(routingContext.getBodyAsString())
            .params(request.params())
            .headers(request.headers())
            .principal(principal)
            .build();

    //        Log requests
    log.info("Handle request={}", requestPath);

    handle(baseRequest)
        .setHandler(
            rs -> {
              if (rs.succeeded()) {
                BaseResponse baseResponse = rs.result();
                //                Log responses
                log.info(
                    "Response to request={} with status={}",
                    requestPath,
                    baseResponse.getStatusCode());
                response
                    .setStatusCode(baseResponse.getStatusCode())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonProtoUtils.printGson(baseResponse));
              } else {
                log.info(
                    "Handle request={} failed, exception={}",
                    requestPath,
                    ExceptionUtil.getDetail(rs.cause()));
                response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
              }
            });
  }

  public abstract Future<BaseResponse> handle(BaseRequest baseRequest);
}
