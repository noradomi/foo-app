package vn.zalopay.phucvt.fooapp.handler;

import vn.zalopay.phucvt.fooapp.cache.UserCache;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.entity.request.BaseRequest;
import vn.zalopay.phucvt.fooapp.entity.response.BaseResponse;
import vn.zalopay.phucvt.fooapp.entity.response.SuccessResponse;
import vn.zalopay.phucvt.fooapp.model.User;
import vn.zalopay.phucvt.fooapp.utils.Tracker;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleHandler extends BaseHandler {
    private static final Logger LOGGER = LogManager.getLogger(ExampleHandler.class);
    private static final String METRIC = "ExampleHandler";
    private final UserCache userCache;
    private final UserDA userDA;
    private final TransactionProvider transactionProvider;

    public ExampleHandler(
            UserDA userDA, UserCache userCache, TransactionProvider transactionProvider) {
        this.userCache = userCache;
        this.userDA = userDA;
        this.transactionProvider = transactionProvider;
    }

    @Override
    public Future<BaseResponse> handle(BaseRequest baseRequest) {

        Tracker.TrackerBuilder tracker =
                Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
        Future<User> future = Future.future();
        User user = User.builder().userId("1").username("username").build();

        if (userDA == null) {
            LOGGER.info("userDA null");
        } else {
            LOGGER.info("userDA not null");
        }
        Transaction transaction = transactionProvider.newTransaction();
        transaction
                .begin()
                .compose(next -> transaction.execute(userDA.insert(user)))
                .compose(userCache::set)
                .setHandler(
                        rs -> {
                            if (rs.succeeded()) {
                                future.complete(rs.result());
                            } else {
                                future.complete(null);
                            }
                            transaction.close();
                            tracker.step("handle").code("SUCCESS").build().record();
                        });

        return future.compose(u -> Future.succeededFuture(SuccessResponse.builder().data(u).build()));
    }
}
