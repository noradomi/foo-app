package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

@Builder
public class ExceptionResponse extends BaseResponse{
    String code;
    String message;
}
