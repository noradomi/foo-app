package vn.zalopay.phucvt.fooapp.da;

import io.vertx.ext.sql.SQLClient;

public class TransactionProviderImpl implements TransactionProvider {
  private final SQLClient sqlClient;

  public TransactionProviderImpl(SQLClient sqlClient) {
    this.sqlClient = sqlClient;
  }

  @Override
  public Transaction newTransaction() {
    return new TransactionImpl(sqlClient);
  }
}
