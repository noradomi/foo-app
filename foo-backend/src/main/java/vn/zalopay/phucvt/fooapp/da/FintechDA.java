package vn.zalopay.phucvt.fooapp.da;

import io.vertx.core.Future;
import vn.zalopay.phucvt.fooapp.model.AccountLog;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;
import vn.zalopay.phucvt.fooapp.model.Transfer;
import vn.zalopay.phucvt.fooapp.model.User;

import java.util.List;

public interface FintechDA {
  Future<User> selectUserForUpdate(String userId);

  Executable<List<User>> selectUsersForUpdate(String senderId, String receiverId);

  Executable<Void> updateBalance(String userId, long newBalance, long lastUpdated);

  Executable<Void> insertTransferLog(Transfer transfer);

  Executable<Void> insertAccountLog(AccountLog accountLog);

  Future<List<HistoryItem>> getHistory(String userId, int pageSize, int pageToken);
}
