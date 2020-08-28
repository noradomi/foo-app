package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.utils.ExceptionUtil;
import io.vertx.core.Future;
import org.apache.logging.log4j.Logger;

public interface Transaction {
  Future<Void> begin();

  Future<Void> commit();

  Future<Void> rollback();

  Future<Void> close();

  default void close(Logger logger, String method) {
    this.close()
        .setHandler(
            rs -> {
              if (rs.failed()) {
                logger.error(
                    "{} close transaction failed cause={}",
                    method,
                    ExceptionUtil.getDetail(rs.cause()));
              }
            });
  }

  <R> Future<R> execute(Executable<R> executable);
}
