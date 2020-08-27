package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.ExceptionResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.ErrorCode;
import vn.zalopay.phucvt.fooapp.utils.GenerationUtils;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

@Log4j2
public class SignUpHandler extends BaseHandler {

    private static final String METRIC = "SignUpHandler";

    private final UserCache userCache;
    private final UserDA userDA;
    private final TransactionProvider transactionProvider;

    public SignUpHandler(UserCache userCache, UserDA userDA, TransactionProvider transactionProvider) {
        this.userCache = userCache;
        this.userDA = userDA;
        this.transactionProvider = transactionProvider;
    }

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {
        Future<BaseResponse> future = Future.future();

        final User user = JsonProtoUtils.parseGson(baseRequest.getPostData(), User.class);

//        Validates post data
        if (StringUtils.isBlank(user.getUsername())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .status(HttpResponseStatus.BAD_REQUEST.code())
                    .code(ErrorCode.REGISTER_USERNAME_EMPTY.code())
                    .message("User name cannot be empty")
                    .build();
            future.complete(response);
        }
        if (StringUtils.isBlank(user.getPassword())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .status(HttpResponseStatus.BAD_REQUEST.code())
                    .code(ErrorCode.REGISTER_PASSWORD_EMPTY.code())
                    .message("Password cannot be empty")
                    .build();
            future.complete(response);
        }
        if (StringUtils.isBlank(user.getFullname())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .status(HttpResponseStatus.BAD_REQUEST.code())
                    .code(ErrorCode.REGISTER_FULLNAME_EMPTY.code())
                    .message("Full name cannot be empty")
                    .build();
            future.complete(response);
        }

        Future<User> getUserAuth = userDA.selectUserByUserName(user.getUsername());

        getUserAuth.compose(existedUserAuth -> {
            if (existedUserAuth != null) {
                ExceptionResponse exceptionResponse = ExceptionResponse
                        .builder()
                        .status(HttpResponseStatus.BAD_REQUEST.code())
                        .code(ErrorCode.REGISTER_USERNAME_UNIQUED.code())
                        .message("User name existed")
                        .build();
                future.complete(exceptionResponse);
            } else {
                Tracker.TrackerBuilder tracker =
                        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());

//                Set unique Id for user from post data using UUID
                user.setUserId(GenerationUtils.generateId());

                Future<User> insertUserFuture = Future.future();

                Transaction transaction = transactionProvider.newTransaction();

                transaction
                        .begin()
                        .compose(next -> transaction.execute(userDA.insert(user)))
                        .compose(userCache::set)
                        .setHandler(
                                rs -> {
                                    if (rs.succeeded()) {
                                        insertUserFuture.complete(rs.result());
                                    } else {
                                        insertUserFuture.complete(null);
                                    }
                                    transaction.close();
                                    tracker.step("handle").code("SUCCESS").build().record();
                                });
                SuccessResponse successResponse = SuccessResponse
                        .builder()
                        .status(HttpResponseStatus.OK.code())
                        .data(insertUserFuture.result())
                        .build();
                future.complete(successResponse);
            }
        }, Future.future().setHandler(handler -> {
            log.info("Sign up failed");
            future.fail(handler.cause());
        }));
        return future;
    }
}
