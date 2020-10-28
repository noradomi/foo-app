package vn.zalopay.phucvt.fooapp.cache;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;

import java.util.List;

public interface FintechCache {
  void setTransactionHistory(List<HistoryItem> items, String userId);

  Future<Void> appendToTransactionHistory(HistoryItem item, String userId);

  Future<List<HistoryItem>> getTransactionHistory(String userId);
}
