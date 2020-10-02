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
    log.info("gRPC call getHistory from userId={}", userId);
    int pageSize = request.getPageSize();
    int pageToken = request.getPageToken();

    fintechDA
        .getHistories(userId)
        .setHandler(
            listAsyncResult -> {
              if (listAsyncResult.succeeded()) {
                List<HistoryItem> historyList = listAsyncResult.result();

                historyList.forEach(x -> System.out.println(x.getUserId()));

                GetHistoryResponse.Data.Builder builder = GetHistoryResponse.Data.newBuilder();
                historyList.forEach(x -> builder.addHistories(mapToTransactionHistory(x)));
                builder.setNextPageToken(20);
                GetHistoryResponse.Data data = builder.build();
                GetHistoryResponse response =
                    GetHistoryResponse.newBuilder()
                        .setData(data)
                        .setStatus(Status.newBuilder().setCode(Code.OK).build())
                        .build();
                responseObserver.onNext(response);
                log.info("Done response history");
                responseObserver.onCompleted();
              } else {
                log.error("get transaction history failed, cause=",listAsyncResult.cause());
                GetHistoryResponse response =
                    GetHistoryResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode(Code.INTERNAL).build())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
              }
            });
  }

  public TransactionHistory mapToTransactionHistory(HistoryItem item) {
    return TransactionHistory.newBuilder()
        .setUserId(item.getUserId())
        .setAmount(item.getAmount())
        .setDescription(item.getDescription())
        .setRecordedTime(item.getRecordedTime())
        .setTransferTypeValue(item.getTransferType())
        .build();
  }
}
