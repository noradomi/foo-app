package vn.zalopay.phucvt.fooapp.grpc.handler.fintech;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.cache.FintechCache;
import vn.zalopay.phucvt.fooapp.da.FintechDA;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;
import vn.zalopay.phucvt.fooapp.model.HistoryItem;
import vn.zalopay.phucvt.fooapp.utils.Tracker;

import java.util.List;

@Builder
@Log4j2
public class GetHistoryHandler {
  private static final String METRIC = "GetHistoryHandler";
  private final FintechDA fintechDA;
  private final FintechCache fintechCache;

  public void handle(
      GetHistoryRequest request, StreamObserver<GetHistoryResponse> responseObserver) {
    Tracker.TrackerBuilder tracker =
        Tracker.builder().metricName(METRIC).startTime(System.currentTimeMillis());
    String userId = AuthInterceptor.USER_ID.get();
    int pageSize = request.getPageSize();
    int pageToken = request.getPageToken();
    log.info("gRPC call: getHistory from userId={} with {}-{}", userId, pageSize, pageToken);
    if (pageToken == 0) {
      getTransactionHistoryFromCache(responseObserver, userId, pageSize, pageToken, tracker);
    } else {
      getTransactionHistoryFromDB(userId, pageSize, pageToken, responseObserver, false, tracker);
    }
  }

  private void getTransactionHistoryFromCache(
      StreamObserver<GetHistoryResponse> responseObserver,
      String userId,
      int pageSize,
      int pageToken,
      Tracker.TrackerBuilder tracker) {
    fintechCache
        .getTransactionHistory(userId)
        .setHandler(
            listAsyncResult -> {
              GetHistoryResponse response;
              if (listAsyncResult.succeeded()) {
                List<HistoryItem> historyList = listAsyncResult.result();
                if (historyList.size() > 0) {
                  log.info("get history list of user={}, cache hit", userId);
                  response = buildSuccessResponse(historyList, historyList.size(), pageToken);
                  responseObserver.onNext(response);
                  responseObserver.onCompleted();
                  tracker.code("ok").build().record();
                } else {
                  getTransactionHistoryFromDB(
                      userId, pageSize, pageToken, responseObserver, true, tracker);
                }
              } else {
                log.error(
                    "get transaction history from cache failed, cause=", listAsyncResult.cause());
                getTransactionHistoryFromDB(
                    userId, pageSize, pageToken, responseObserver, true, tracker);
              }
            });
  }

  private void getTransactionHistoryFromDB(
      String userId,
      int pageSize,
      int pageToken,
      StreamObserver<GetHistoryResponse> responseObserver,
      boolean initCache,
      Tracker.TrackerBuilder tracker) {
    fintechDA
        .getHistory(userId, pageSize, pageToken)
        .setHandler(
            listAsyncResult -> {
              GetHistoryResponse response;
              if (listAsyncResult.succeeded()) {
                log.info("get history list of user={}, from db", userId);
                List<HistoryItem> historyList = listAsyncResult.result();
                if (initCache) fintechCache.setTransactionHistory(historyList, userId);
                response =
                    historyList.size() == 0
                        ? buildSuccessResponse(historyList, 0, 0)
                        : buildSuccessResponse(historyList, historyList.size(), pageToken);
              } else {
                log.error(
                    "get transaction history from db failed, cause=", listAsyncResult.cause());
                response =
                    GetHistoryResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
              }
              responseObserver.onNext(response);
              responseObserver.onCompleted();
              tracker.code("ok").build().record();
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
