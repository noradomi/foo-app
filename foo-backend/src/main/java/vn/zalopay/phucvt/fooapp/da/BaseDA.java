package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.json.JsonArray;
import org.apache.logging.log4j.Logger;
import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDA {
  protected final int statementTimeoutSec;

  public BaseDA() {
    statementTimeoutSec = 60;
  }

  public BaseDA(int statementTimeoutSec) {
    this.statementTimeoutSec = statementTimeoutSec;
  }

  protected void closeResource(Logger logger, PreparedStatement preparedStatement) {
    closeResource(logger, null, preparedStatement, null);
  }

  protected void closeResource(
      Logger logger, PreparedStatement preparedStatement, Connection connection) {
    closeResource(logger, null, preparedStatement, connection);
  }

  protected void closeResource(
      Logger logger, ResultSet resultSet, PreparedStatement preparedStatement) {
    closeResource(logger, resultSet, preparedStatement, null);
  }

  protected void closeResource(Logger logger, Connection connection) {
    closeResource(logger, null, null, connection);
  }

  protected void closeResource(
      Logger logger,
      ResultSet resultSet,
      PreparedStatement preparedStatement,
      Connection connection) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (Exception e) {
        logger.warn("closeResource resultSet failed cause={}", ExceptionUtil.getDetail(e));
      }
    }
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (Exception e) {
        logger.warn("closeResource preparedStatement failed cause={}", ExceptionUtil.getDetail(e));
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        logger.error("closeResource connection failed cause={}", ExceptionUtil.getDetail(e));
      }
    }
  }

  protected void setParamsFromJsonArray(PreparedStatement preparedStatement, JsonArray params)
      throws SQLException {
    int size = params.getList().size();
    for (int i = 0; i < size; i++) {
      preparedStatement.setObject(i + 1, params.getList().get(i));
    }
  }

  protected void setParamsFromArray(PreparedStatement preparedStatement, Object[] params)
      throws SQLException {
    int size = params.length;
    for (int i = 0; i < size; i++) {
      preparedStatement.setObject(i + 1, params[i]);
    }
  }
}
