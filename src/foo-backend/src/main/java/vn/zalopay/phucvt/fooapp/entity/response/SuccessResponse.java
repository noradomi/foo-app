package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

@Builder
public class SuccessResponse implements BaseResponse{
    int status;
//    String message;
    Object data;
}
