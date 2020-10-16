package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class GetBalanceHandlerTest {

  @Mock private UserDA userDA;

  @InjectMocks private GetBalanceHandler getBalanceHandler;

  @Test
  public void handle() {
    User user = User.builder().userId("123").name("Noradomi").balance(300000).build();
    long responseBalance = getBalanceHandler.buildResponse(user).getData().getBalance();
    assertThat(responseBalance, is(300000L));
  }
}
