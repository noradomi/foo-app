package vn.zalopay.phucvt.fooapp.utils;

import io.vertx.core.Vertx;
import lombok.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

@Builder
public class AsyncHandler {
  private static final Logger LOGGER =
      LogManager.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());
  private final Vertx vertx;

  public void run(Runnable runnable) {
    if (vertx != null) {
      vertx.executeBlocking(
          future -> {
            try {
              runnable.run();
              future.complete();
            } catch (Exception e) {
              future.fail(e);
            }
          },
          false,
          res -> {
            if (res.failed()) {
              LOGGER.error("Failed executeBlocking cause={}", ExceptionUtil.getDetail(res.cause()));
            }
          });
    }
  }
}
