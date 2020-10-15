package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.vertx.core.Future;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.ChatDA;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.da.TransactionProvider;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.TransferMoneyRequest;
import vn.zalopay.phucvt.fooapp.grpc.exceptions.TransferMoneyException;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.TransferMoneyHolder;
import vn.zalopay.phucvt.fooapp.model.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyHandlerTest {
  @InjectMocks TransferMoneyHandler transferMoneyHandler;
  @Mock private UserDA userDA;
  @Mock private FintechDA fintechDA;
  @Mock private FintechCache fintechCache;
  private ChatDA chatDA;
  private ChatCache chatCache;
  private WSHandler wsHandler;
  private TransactionProvider transactionProvider;

  @Test
  public void testValidatePassword_whenConfirmPasswordIsCorrect() {
    User userAuth = User.builder().password(BCrypt.hashpw("abc", BCrypt.gensalt(5))).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setConfirmPassword("abc").build();
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setSender(userAuth);
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validatePassword(holder);
    assertThat(true, Is.is(future.succeeded()));
  }

  @Test
  public void testValidatePassword_whenConfirmPasswordIsInvalid() {
    User userAuth = User.builder().password(BCrypt.hashpw("123", BCrypt.gensalt(5))).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setConfirmPassword("abc").build();
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setSender(userAuth);
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validatePassword(holder);
    assertThat(true, Is.is(future.failed()));

    Throwable throwable = future.cause();
    assertThat(true, Is.is(throwable instanceof TransferMoneyException));

    TransferMoneyException transferMoneyException = (TransferMoneyException) throwable;
    assertThat(Code.INVALID_PASSWORD, Is.is((transferMoneyException.getCode())));
  }

  @Test
  public void testValidateAmountTransfer_whenAmountIsValid() {
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(100000).build();
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validateAmountTransfer(holder);
    assertThat(true, Is.is(future.succeeded()));
  }

  @Test
  public void testValidateAmountTransfer_whenAmountIsInvalid() {
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(999).build();
    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validateAmountTransfer(holder);
    assertThat(true, is(future.failed()));

    Throwable throwable = future.cause();
    assertThat(true, is(throwable instanceof TransferMoneyException));

    TransferMoneyException transferMoneyException = (TransferMoneyException) throwable;
    assertThat(Code.INVALID_INPUT_MONEY, is((transferMoneyException.getCode())));
  }

  @Test
  public void testValidateAmountTransfer_whenReceiverIdIsValid() {
    User user = User.builder().build();
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setReceiverId("123").build();

    when(userDA.selectUserById(any())).thenReturn(Future.succeededFuture(user));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validateReceiverId(holder);
    assertThat(true, is(future.succeeded()));
  }

  @Test
  public void testValidateAmountTransfer_whenReceiverIdIsNotFound() {
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setReceiverId("123").build();

    when(userDA.selectUserById(any())).thenReturn(Future.succeededFuture());

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);

    Future<TransferMoneyHolder> future = transferMoneyHandler.validateReceiverId(holder);
    assertThat(true, is(future.failed()));

    Throwable throwable = future.cause();
    assertThat(true, is(throwable instanceof TransferMoneyException));

    TransferMoneyException transferMoneyException = (TransferMoneyException) throwable;
    assertThat(Code.USER_ID_NOT_FOUND, is((transferMoneyException.getCode())));
  }

  @Test
  public void testCheckBalance_whenBalanceEnoughMoney() {
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setAmount(1000).build(); // equal balance

    when(fintechDA.selectUserForUpdate(any())).thenReturn(Future.succeededFuture(user));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    TransferMoneyHolder successHolder = future.result();
    assertThat(1000L, is(successHolder.getSender().getBalance()));
  }

  @Test
  public void testCheckBalance_whenBalanceLessThanAmount() {
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
            TransferMoneyRequest.newBuilder().setAmount(2000).build(); // greater than balance

    when(fintechDA.selectUserForUpdate(any())).thenReturn(Future.succeededFuture(user));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    assertThat(true,is(future.failed()));
    TransferMoneyException transferMoneyException = (TransferMoneyException)future.cause();
    assertThat(Code.NOT_ENOUGH_MONEY, is(transferMoneyException.getCode()));
  }

  @Test
  public void testCheckBalance_whenSelectUserForUpdateFromDBFailed() {
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
            TransferMoneyRequest.newBuilder().setAmount(2000).build();

    when(fintechDA.selectUserForUpdate(any())).thenReturn(Future.failedFuture(new Exception()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    assertThat(true,is(future.failed()));
    assertThat(true, is(future.cause() instanceof Exception));
  }
}
