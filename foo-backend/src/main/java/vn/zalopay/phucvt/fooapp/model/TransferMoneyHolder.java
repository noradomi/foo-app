package vn.zalopay.phucvt.fooapp.model;

import lombok.Getter;
import lombok.Setter;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.fintech.TransferMoneyRequest;

@Getter
@Setter
public class TransferMoneyHolder {
  private User userAuth;
  private TransferMoneyRequest request;
  private long senderBalance;
  private long receiverBalance;
  private Transaction transaction;
  private String transferId;
  private long recordedTime;
}
