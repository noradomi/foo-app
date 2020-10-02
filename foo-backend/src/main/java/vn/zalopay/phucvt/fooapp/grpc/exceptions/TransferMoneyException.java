package vn.zalopay.phucvt.fooapp.grpc.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.zalopay.phucvt.fooapp.fintech.Code;


@Getter
@Setter
public class TransferMoneyException extends Exception {
  private Code code;

  public TransferMoneyException(String s, Code code) {
    super(s);
    this.code = code;
  }
}
