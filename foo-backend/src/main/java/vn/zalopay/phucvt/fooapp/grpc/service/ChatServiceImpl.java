package vn.zalopay.phucvt.fooapp.grpc.service;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.fintech.*;
import vn.zalopay.phucvt.fooapp.grpc.handler.chat.AddFriendHandler;
import vn.zalopay.phucvt.fooapp.grpc.handler.chat.GetFriendListHandler;
import vn.zalopay.phucvt.fooapp.grpc.handler.chat.ResetUnseenHandler;

@Log4j2
@Builder
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
  private final AddFriendHandler addFriendHandler;
  private final ResetUnseenHandler resetUnseenHandler;
  private final GetFriendListHandler getFriendListHandler;

  @Override
  public void addFriend(
      AddFriendRequest request, StreamObserver<AddFriendResponse> responseObserver) {
    addFriendHandler.handle(request, responseObserver);
  }

  @Override
  public void getFriendList(
      GetFriendListRequest request, StreamObserver<GetFriendListResponse> responseObserver) {
    getFriendListHandler.handle(request, responseObserver);
  }

  @Override
  public void resetUnseen(
      ResetUnseenRequest request, StreamObserver<ResetUnseenResponse> responseObserver) {
    resetUnseenHandler.handle(request, responseObserver);
  }
}
