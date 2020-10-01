package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.model.AccountLog;
import vn.zalopay.phucvt.fooapp.model.Transfer;

public interface FintechDA {
  Executable<Void> updateBalance(String userId, long amount,long lastUpdated);

  Executable<Void> insertTransferLog(
      Transfer transfer);

  Executable<Void> insertAccountLog(AccountLog accountLog);
}
