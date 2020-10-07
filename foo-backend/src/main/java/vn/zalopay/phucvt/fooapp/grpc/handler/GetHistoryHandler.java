package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;

import java.util.List;

@Builder
@Log4j2
public class GetHistoryHandler {
  private final FintechDA fintechDA;

  public void handle(
      GetHistoryRequest request, StreamObserver<GetHistoryResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    int pageSize = request.getPageSize();
    int pageToken = request.getPageToken();
    log.info("gRPC call getHistory from userId={} with {}-{}", userId, pageSize, pageToken);
    fintechDA
        .getHistory(userId, pageSize, pageToken)
        .setHandler(
            listAsyncResult -> {
              GetHistoryResponse response;
              if (listAsyncResult.succeeded()) {
                List<HistoryItem> historyList = listAsyncResult.result();
                response =
                    historyList.size() == 0
                        ? handleSuccessResponse(historyList, 0, 0)
                        : handleSuccessResponse(historyList, historyList.size(), pageToken);
              } else {
                log.error("get transaction history failed, cause=", listAsyncResult.cause());
                response =
                    GetHistoryResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
              }
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            });
  }

  private GetHistoryResponse handleSuccessResponse(
      List<HistoryItem> historyList, int size, int pageToken) {
    GetHistoryResponse.Data.Builder builder = GetHistoryResponse.Data.newBuilder();
    historyList.forEach(x -> builder.addItems(mapToTransactionHistory(x)));
    builder.setNextPageToken(pageToken + size);
    GetHistoryResponse.Data data = builder.build();
    return GetHistoryResponse.newBuilder()
        .setData(data)
        .setStatus(Status.newBuilder().setCode(Code.OK).build())
        .build();
  }

  public TransactionHistory mapToTransactionHistory(HistoryItem item) {
    return TransactionHistory.newBuilder()
        .setUserId(item.getTransferType() == 0 ? item.getReceiverId() : item.getSenderId())
        .setAmount(item.getAmount())
        .setDescription(item.getDescription())
        .setRecordedTime(item.getRecordedTime())
        .setTransferTypeValue(item.getTransferType())
        .build();
  }
}
