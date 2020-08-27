package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.utils.Tracker;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

//
// TransactionImpl interface help you controllable what's function belong to transaction
//

public class TransactionImpl implements Transaction {
  private static final String SYSTEM_METRIC_NAME = "DB";
  // For blocking operation
  private final SQLClient dataSource;

  private SQLConnection connection;

  public TransactionImpl(SQLClient sqlClient) {
    this.dataSource = sqlClient;
  }

  public Future<Void> begin() {
    Future future = Future.future();
    Tracker.TrackerBuilder tracker = getTracker();
    dataSource.getConnection(
        rs -> {
          if (rs.failed()) {
            tracker.step("begin-fail").build().record();
            future.fail(rs.cause());
          } else {
            tracker.step("begin-ok").build().record();
            connection = rs.result();
            setAutoCommitFalse(future);
          }
        });
    return future;
  }

  private void setAutoCommitFalse(Future future) {
    Connection unwrapConn = connection.unwrap();
    try {
      if (unwrapConn.getAutoCommit()) {
        connection.setAutoCommit(false, future);
      }
    } catch (SQLException e) {
      future.fail(e);
    }
  }

  public Future<Void> commit() {
    if (connection == null) {
      return Future.failedFuture(new SQLException("Transaction NOT start yet"));
    }

    Tracker.TrackerBuilder tracker = getTracker();
    Future<Void> future = Future.future();
    connection.commit(res -> tracking(res, future, "commit", tracker));

    return future;
  }

  public Future<Void> rollback() {
    if (connection == null) {
      return Future.succeededFuture();
    }

    Tracker.TrackerBuilder tracker = getTracker();
    Future<Void> future = Future.future();
    connection.rollback(res -> tracking(res, future, "rollback", tracker));

    return future;
  }

  @Override
  public Future<Void> close() {
    if (connection == null) {
      return Future.succeededFuture();
    }

    Tracker.TrackerBuilder tracker = getTracker();
    Future<Void> future = Future.future();
    connection.close(res -> tracking(res, future, "close", tracker));
    return future;
  }

  private Tracker.TrackerBuilder getTracker() {
    return Tracker.builder().metricName(SYSTEM_METRIC_NAME);
  }

  @Override
  public <R> Future<R> execute(Executable<R> executable) {
    return executable.execute(connection);
  }

  private void tracking(
      AsyncResult<Void> res,
      Future<Void> future,
      String methodName,
      Tracker.TrackerBuilder tracker) {
    if (res.succeeded()) {
      tracker.step(methodName + "-ok").build().record();
      future.complete(res.result());
    } else {
      tracker.step(methodName + "-fail").build().record();
      future.fail(res.cause());
    }
  }
}
