package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.ExceptionResponse;
import vn.zalopay.phucvt.fooapp.entity.response.JwtResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.ErrorCode;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.jwt.JWTOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginHandler extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(LoginHandler.class);

    private JWTAuth authProvider;

    private final UserCache userCache;

    private final UserDA userDA;

    public LoginHandler(UserDA userDA, UserCache userCache, JWTAuth authProvider) {
        this.authProvider = authProvider;
        this.userCache = userCache;
        this.userDA = userDA;
    }

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {

        final User user = JsonProtoUtils.parseGson(baseRequest.getPostData(), User.class);

        Future<User> getUserAuth = userDA.selectUserByUserName(user.getUsername());

        Future<BaseResponse> future = Future.future();

        getUserAuth.compose(userAuth -> {
            if (userAuth != null && userAuth.getPassword().equals(user.getPassword())) {
                String token = authProvider.generateToken(
                        new JsonObject()
                                .put("userId", user.getUserId()),
                        new JWTOptions()
                                .setExpiresInSeconds(174600));

                JwtResponse jwtResponse = JwtResponse
                        .builder()
                        .token(token)
                        .userId(userAuth.getUserId())
                        .build();

                SuccessResponse successResponse = SuccessResponse
                        .builder()
                        .status(HttpResponseStatus.OK.code())
                        .data(jwtResponse)
                        .build();

                future.complete(successResponse);

            } else {
                ExceptionResponse exceptionResponse = ExceptionResponse
                        .builder()
                        .status(HttpResponseStatus.UNAUTHORIZED.code())
                        .code(ErrorCode.AUTHORIZED_FAILED.code())
                        .message("Invalid Username or Password")
                        .build();
                future.complete(exceptionResponse);
            }
        }, Future.future().setHandler(handler -> {
            LOGGER.info("Signin failed with username " + user.getUsername());
            future.fail(handler.cause());
        }));

        return future;

    }

}
