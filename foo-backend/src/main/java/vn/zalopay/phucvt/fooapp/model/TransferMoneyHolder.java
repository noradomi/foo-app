package vn.zalopay.phucvt.fooapp.model;

import lombok.Getter;
import lombok.Setter;
import vn.zalopay.phucvt.fooapp.da.Transaction;
import vn.zalopay.phucvt.fooapp.fintech.TransferMoneyRequest;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.sql.Connection;

@Getter
@Setter
public class TransferMoneyHolder {
  private User sender;
  private TransferMoneyRequest request;
  private User receiver;
  private Transaction transaction;
  private String transferId;
  private long recordedTime;
  private Connection connection;
  private Tracker.TrackerBuilder tracker;
  private String receiverIdForTest;
}
