package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import vn.zalopay.phucvt.fooapp.cache.ChatCache;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.*;
import vn.zalopay.phucvt.fooapp.fintech.Code;
import vn.zalopay.phucvt.fooapp.fintech.TransferMoneyRequest;
import vn.zalopay.phucvt.fooapp.grpc.exceptions.TransferMoneyException;
import vn.zalopay.phucvt.fooapp.handler.WSHandler;
import vn.zalopay.phucvt.fooapp.model.TransferMoneyHolder;
import vn.zalopay.phucvt.fooapp.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(VertxUnitRunner.class)
public class TransferMoneyHandlerTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
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
  public void testCheckBalance_whenBalanceEnoughMoney(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    List<User> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user);
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setAmount(1000).build(); // equal balance

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture(userList));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setTransaction(transaction);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    future.setHandler(
        asyncResult -> {
          if (asyncResult.succeeded()) {
            TransferMoneyHolder successHolder = asyncResult.result();
            context.assertEquals(1000L, successHolder.getSender().getBalance());
          } else {
            context.fail(asyncResult.cause());
          }
          async.complete();
        });
  }

  @Test
  public void testCheckBalance_whenBalanceLessThanAmount(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    List<User> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user);
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setAmount(2000).build(); // equal balance

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture(userList));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setTransaction(transaction);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    future.setHandler(
        asyncResult -> {
          TransferMoneyException transferMoneyException = (TransferMoneyException) future.cause();
          context.assertEquals(Code.NOT_ENOUGH_MONEY, transferMoneyException.getCode());
          async.complete();
        });
  }

  @Test
  public void testCheckBalance_whenSelectUsersForUpdateFromDBFailed(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    List<User> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user);
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder().setAmount(2000).build(); // equal balance

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.failedFuture(new Exception()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setTransaction(transaction);
    holder.setSender(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.checkBalance(holder);
    future.setHandler(
        asyncResult -> {
          assertThat(true, is(future.cause() instanceof Exception));
          async.complete();
        });
  }

  @Test
  public void testDebit_whenSuccess(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(1000).build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture());

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.debit(holder);
    future.setHandler(
        asyncResult -> {
          TransferMoneyHolder resultHolder = asyncResult.result();
          context.assertEquals(0L, resultHolder.getSender().getBalance());
          async.complete();
        });
  }

  @Test
  public void testDebit_whenFailed(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(1000).build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.failedFuture(new SQLException()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.debit(holder);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.failed());
          async.complete();
        });
  }

  @Test
  public void testCredit_whenSuccess(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(1000).build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture());

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.credit(holder);
    future.setHandler(
        asyncResult -> {
          TransferMoneyHolder resultHolder = asyncResult.result();
          context.assertEquals(2000L, resultHolder.getReceiver().getBalance());
          async.complete();
        });
  }

  @Test
  public void testCredit_whenFailed(TestContext context) {
    final Async async = context.async();
    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request = TransferMoneyRequest.newBuilder().setAmount(1000).build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.failedFuture(new SQLException()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.credit(holder);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.failed());
          async.complete();
        });
  }

  @Test
  public void testWriteTransferLog_whenSuccess(TestContext context) {
    final Async async = context.async();

    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder()
            .setReceiverId("123")
            .setDescription("abc")
            .setAmount(1000)
            .build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture());

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.writeTransferLog(holder);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.succeeded());
          async.complete();
        });
  }

  @Test
  public void testWriteTransferLog_whenFailed(TestContext context) {
    final Async async = context.async();

    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder()
            .setReceiverId("123")
            .setDescription("abc")
            .setAmount(1000)
            .build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.failedFuture(new SQLException()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.writeTransferLog(holder);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.failed());
          async.complete();
        });
  }

  @Test
  public void testWriteAccountLog_whenSuccess(TestContext context) {
    final Async async = context.async();

    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder()
            .setReceiverId("123")
            .setDescription("abc")
            .setAmount(1000)
            .build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.succeededFuture());

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.writeAccountLog(holder, 0);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.succeeded());
          async.complete();
        });
  }

  @Test
  public void testWriteAccountLog_whenFailed(TestContext context) {
    final Async async = context.async();

    User user = User.builder().userId("123").balance(1000).build();
    TransferMoneyRequest request =
        TransferMoneyRequest.newBuilder()
            .setReceiverId("123")
            .setDescription("abc")
            .setAmount(1000)
            .build();

    Transaction transaction = Mockito.mock(Transaction.class);

    when(transaction.execute(any())).thenReturn(Future.failedFuture(new SQLException()));

    TransferMoneyHolder holder = new TransferMoneyHolder();
    holder.setRequest(request);
    holder.setSender(user);
    holder.setTransaction(transaction);
    holder.setReceiver(user);

    Future<TransferMoneyHolder> future = transferMoneyHandler.writeAccountLog(holder, 0);
    future.setHandler(
        asyncResult -> {
          context.assertEquals(true, asyncResult.failed());
          async.complete();
        });
  }
}
