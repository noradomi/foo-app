package vn.zalopay.phucvt.fooapp.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
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

        log.info("get user post data {}",user.getFullname());

//        Validates post data
        if (StringUtils.isBlank(user.getUsername())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .code(ErrorCode.REGISTER_USERNAME_EMPTY.code())
                    .message("User name cannot be empty")
                    .build();
            response.setStatus(HttpResponseStatus.BAD_REQUEST.code());
            future.complete(response);
            return future;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .code(ErrorCode.REGISTER_PASSWORD_EMPTY.code())
                    .message("Password cannot be empty")
                    .build();
            response.setStatus(HttpResponseStatus.BAD_REQUEST.code());
            future.complete(response);
            return future;
        }
        if (StringUtils.isBlank(user.getFullname())) {
            ExceptionResponse response = ExceptionResponse
                    .builder()
                    .code(ErrorCode.REGISTER_FULLNAME_EMPTY.code())
                    .message("Full name cannot be empty")
                    .build();
            response.setStatus(HttpResponseStatus.BAD_REQUEST.code());
            future.complete(response);
            return future;
        }

        log.info("Input data correct");

        Future<User> getUserAuth = userDA.selectUserByUserName(user.getUsername());

        log.info("Start insert user");
        getUserAuth.compose(existedUserAuth -> {
//            log.info("Block insert user");
            if (existedUserAuth != null) {
                ExceptionResponse exceptionResponse = ExceptionResponse
                        .builder()
                        .code(ErrorCode.REGISTER_USERNAME_UNIQUED.code())
                        .message("User name existed")
                        .build();
                exceptionResponse.setStatus(HttpResponseStatus.BAD_REQUEST.code());
                future.complete(exceptionResponse);
            } else {
                log.info("Block insert user");
//                Tracker.TrackerBuilder tracker =
//                        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());

//                Set unique Id for user from post data using UUID
                user.setUserId(GenerationUtils.generateId());

//                Set hashed password
                String hashedPassword = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5));
                log.info("hashed password: {}",hashedPassword);
                user.setPassword(hashedPassword);

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
                                        log.info("insert success with {}",rs.result().getFullname());
                                    } else {
                                        insertUserFuture.complete(null);
                                        log.info("insert failed with null");
                                    }
                                    transaction.commit();
                                    log.info("transaction commit");
                                    transaction.close();
//                                    tracker.step("handle").code("SUCCESS").build().record();
                                });
                insertUserFuture.compose(u ->{
                    SuccessResponse successResponse = SuccessResponse
                            .builder()
                            .data(u)
                            .build();
                    successResponse.setStatus(HttpResponseStatus.OK.code());
                    future.complete(successResponse);
                },Future.future().setHandler(handler -> {
                    future.fail(handler.cause());
                }));

            }
        }, Future.future().setHandler(handler -> {
            log.info("Sign up failed");
            future.fail(handler.cause());
        }));
        return future;
    }
}
