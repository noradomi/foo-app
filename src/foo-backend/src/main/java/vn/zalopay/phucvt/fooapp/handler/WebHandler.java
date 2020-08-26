package vn.zalopay.phucvt.fooapp.handler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.service.WebService;
import vn.zalopay.phucvt.fooapp.util.HttpStatus;

public class WebHandler {
    private static final Logger LOGGER = LogManager.getLogger(WebHandler.class);

    private WebService webService;

    public void signIn(RoutingContext routingContext){
        String requestJson = routingContext.getBodyAsString();
        Future<JsonObject> signInFuture = webService.signIn(requestJson);

        signInFuture.complete(jsonObject -> {
            routingContext.response()
                    .setStatusCode(HttpStatus. )
        });
    }
}
