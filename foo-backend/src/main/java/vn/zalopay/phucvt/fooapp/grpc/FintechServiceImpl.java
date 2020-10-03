package vn.zalopay.phucvt.fooapp.grpc;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.handler.*;

@Log4j2
@Builder
public class FintechServiceImpl extends FintechServiceGrpc.FintechServiceImplBase {
  private final GetBalanceHandler getBalanceHandler;
  private final TransferMoneyHandler transferMoneyHandler;
  private final GetHistoryHandler getHistoryHandler;
  private final AddFriendHandler addFriendHandler;
  private final MarkReadHandler markReadHandler;
  private final GetFriendListHandler getFriendListHandler;

  @Override
  public void getBalance(
      GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {
    getBalanceHandler.handle(request, responseObserver);
  }

  @Override
  public void transferMoney(
      TransferMoneyRequest request, StreamObserver<TransferMoneyResponse> responseObserver) {
    transferMoneyHandler.handle(request, responseObserver);
  }

  @Override
  public void getHistory(
      GetHistoryRequest request, StreamObserver<GetHistoryResponse> responseObserver) {
    getHistoryHandler.handle(request, responseObserver);
  }

  @Override
  public void addFriend(
      AddFriendRequest request, StreamObserver<AddFriendResponse> responseObserver) {
    addFriendHandler.handle(request, responseObserver);
  }

  @Override
  public void getStrangerList(
      GetStrangerListRequest request, StreamObserver<GetStrangerListResponse> responseObserver) {
    super.getStrangerList(request, responseObserver);
  }

  @Override
  public void getFriendList(
      GetFriendListRequest request, StreamObserver<GetFriendListResponse> responseObserver) {
    getFriendListHandler.handle(request, responseObserver);
  }

  @Override
  public void getUserInfo(
      GetUserInfoRequest request, StreamObserver<GetUserInfoResponse> responseObserver) {
    super.getUserInfo(request, responseObserver);
  }

  @Override
  public void markRead(MarkReadRequest request, StreamObserver<MarkReadResponse> responseObserver) {
    markReadHandler.handle(request, responseObserver);
  }
}
