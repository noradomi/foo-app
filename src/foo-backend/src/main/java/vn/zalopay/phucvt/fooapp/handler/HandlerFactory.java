package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.config.APIPath;
import com.google.common.collect.ImmutableMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import lombok.Builder;

@Builder
public class HandlerFactory {
    private BaseHandler echoHandler;
    private BaseHandler exampleHandler;
    private BaseHandler loginHandler;

    public void initialize(Router router) {
        ImmutableMap<String, BaseHandler> postHandler =
                ImmutableMap.<String, BaseHandler>builder()
                        .put(APIPath.ECHO, echoHandler)
                        .put(APIPath.EXAMPLE, exampleHandler)
                        .put(APIPath.LOGIN, loginHandler)
                        .build();

        postHandler
                .entrySet()
                .forEach(
                        entry ->
                                router
                                        .route()
                                        .method(HttpMethod.POST)
                                        .path(entry.getKey())
                                        .handler(entry.getValue()::handle));
    }
}
