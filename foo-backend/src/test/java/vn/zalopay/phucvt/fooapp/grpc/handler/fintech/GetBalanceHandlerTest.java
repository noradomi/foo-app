package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.model.User;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(VertxUnitRunner.class)
public class GetBalanceHandlerTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private UserDA userDA;
  @InjectMocks private GetBalanceHandler getBalanceHandler;

  @Test
  public void testGetBalance_whenSuccess(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").name("Noradomi").balance(300000).build();

    when(userDA.selectUserById(any())).thenReturn(Future.succeededFuture(user));

    Future<User> future = userDA.selectUserById("123");
    future.setHandler(
        userAsyncResult -> {
          context.assertEquals("123", userAsyncResult.result().getUserId());
          async.complete();
        });
  }

  @Test
  public void testGetBalance_whenFailed(TestContext context) {
    final Async async = context.async();
    when(userDA.selectUserById(any())).thenReturn(Future.failedFuture(new SQLException()));
    Future<User> future = userDA.selectUserById("123");
    future.setHandler(
        userAsyncResult -> {
          context.assertEquals(true, userAsyncResult.failed());
          async.complete();
        });
  }
}
