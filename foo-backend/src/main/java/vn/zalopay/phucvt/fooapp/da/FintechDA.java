package vn.zalopay.phucvt.fooapp.da;

import vn.zalopay.phucvt.fooapp.model.Transfer;

public interface FintechDA {
  Executable<Void> updateBalance(String userId, long amount);

  Executable<Transfer> insertTransferLog(
      Transfer transfer);

  Executable<>
}
