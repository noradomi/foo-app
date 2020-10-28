package vn.zalopay.phucvt.fooapp.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.SQLException;

public class ExceptionUtil {

  private ExceptionUtil() {}

  public static String getDetail(Throwable ex) {
    if (ex == null) {
      return "";
    }

    String details = "";
    if (ex instanceof SQLException) {
      SQLException sqlException = (SQLException) ex;
      details =
          "Code="
              + sqlException.getErrorCode()
              + ", \nsqlState="
              + sqlException.getSQLState()
              + "\n, ";
    }

    details += getMessage(ex);

    return details;
  }

  private static String getMessage(Throwable ex) {
    return " message: " + ex.getMessage() + "\n, stackTrace: " + ExceptionUtils.getStackTrace(ex);
  }

  public static String getSafeMessage(Throwable ex) {
    if (ex == null || ex.getMessage() == null) {
      return "";
    }
    return ex.getMessage();
  }

  public static boolean isEqual(Object current, Class expect) {
    return current != null && isEqual(current.getClass(), expect);
  }

  public static boolean isEqual(Class current, Class expect) {
    return expect.isAssignableFrom(current);
  }
}
