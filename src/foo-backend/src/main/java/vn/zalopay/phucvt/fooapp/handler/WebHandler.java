package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.service.WebService;
import vn.zalopay.phucvt.fooapp.util.HttpStatus;
import vn.zalopay.phucvt.fooapp.util.JsonUtils;

public class WebHandler {
    private static final Logger LOGGER = LogManager.getLogger(WebHandler.class);

    private WebService webService;

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public void signIn(RoutingContext routingContext) {
        String requestJson = routingContext.getBodyAsString();
        Future<JsonObject> signInFuture = webService.signIn(requestJson);

        signInFuture.compose(jsonObject -> {

            routingContext.response()
                    .setStatusCode(HttpStatus.OK.code())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonUtils.successResponse(jsonObject));

        }, Future.future().setHandler(handler -> {
            LOGGER.info("Exception");
        }));

    }

//        signInFuture.compose(,Future.future().setHandler(());
    }
}
