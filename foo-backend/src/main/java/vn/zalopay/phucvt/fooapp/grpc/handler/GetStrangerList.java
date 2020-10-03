package vn.zalopay.phucvt.fooapp.grpc.handler;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import vn.zalopay.phucvt.fooapp.da.UserDA;
import vn.zalopay.phucvt.fooapp.fintech.GetStrangerListRequest;
import vn.zalopay.phucvt.fooapp.fintech.GetStrangerListResponse;
import vn.zalopay.phucvt.fooapp.grpc.AuthInterceptor;

@Builder
@Log4j2
public class GetStrangerList {
	private final UserDA userDA;

	public void handle(GetStrangerListRequest request, StreamObserver<GetStrangerListResponse> responseObserver){
		String userId = AuthInterceptor.USER_ID.get();
		log.info("gRPC call getStrangerList from userId={}", userId);

	}
}
