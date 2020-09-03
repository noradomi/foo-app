package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

@Builder
public class SuccessResponse extends BaseResponse{
//    String message;
    Object data;
}
