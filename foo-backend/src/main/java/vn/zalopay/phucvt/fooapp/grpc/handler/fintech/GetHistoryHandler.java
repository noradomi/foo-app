package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;

import java.util.List;

@Builder
@Log4j2
public class GetHistoryHandler {
  private final FintechDA fintechDA;
  private final FintechCache fintechCache;

  public void handle(
      GetHistoryRequest request, StreamObserver<GetHistoryResponse> responseObserver) {
    String userId = AuthInterceptor.USER_ID.get();
    int pageSize = request.getPageSize();
    int pageToken = request.getPageToken();
    log.info("gRPC call: getHistory from userId={} with {}-{}", userId, pageSize, pageToken);
    if (pageToken == 0) {
      fintechCache
          .getTransactionHistory(userId)
          .setHandler(
              listAsyncResult -> {
                GetHistoryResponse response;
                if (listAsyncResult.succeeded()) {
                  log.info("get transaction list of user={}, cache hit", userId);
                  List<HistoryItem> historyList = listAsyncResult.result();
                  response = buildSuccessResponse(historyList, historyList.size(), pageToken);
                  responseObserver.onNext(response);
                  responseObserver.onCompleted();
                } else {
                  getTransactionHistoryFromDB(userId, pageSize, pageToken, responseObserver);
                }
              });
    } else {
      getTransactionHistoryFromDB(userId, pageSize, pageToken, responseObserver);
    }
  }

  private void getTransactionHistoryFromDB(
      String userId,
      int pageSize,
      int pageToken,
      StreamObserver<GetHistoryResponse> responseObserver) {
    fintechDA
        .getHistory(userId, pageSize, pageToken)
        .setHandler(
            listAsyncResult -> {
              GetHistoryResponse response;
              if (listAsyncResult.succeeded()) {
                List<HistoryItem> historyList = listAsyncResult.result();
                fintechCache.setTransactionHistory(historyList, userId);
                response =
                    historyList.size() == 0
                        ? buildSuccessResponse(historyList, 0, 0)
                        : buildSuccessResponse(historyList, historyList.size(), pageToken);
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

  public GetHistoryResponse buildSuccessResponse(
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
