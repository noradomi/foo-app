package vn.zalopay.phucvt.fooapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.fintech.TransferMoneyRequest;

@Getter
@Setter
public class TransferMoneyHolder {
  private User sender;
  private TransferMoneyRequest request;
  private User receiver;
//  private long senderBalance;
//  private long receiverBalance;
  private Transaction transaction;
  private String transferId;
  private long recordedTime;
}
