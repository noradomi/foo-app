package vn.zalopay.phucvt.fooapp.model;

import lombok.Getter;
import lombok.Setter;
import vn.zalopay.phucvt.fooapp.da.Transaction;

@Getter
@Setter
public class TransferMoneyHolder {
	private User userAuth;
	private String receiverId;
	private String confirmPassword;
	private Long amount;
	private Transaction transaction;
//	private Object
}
