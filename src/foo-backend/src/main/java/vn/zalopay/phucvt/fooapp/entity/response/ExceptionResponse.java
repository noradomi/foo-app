package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

@Builder
public class ExceptionResponse implements BaseResponse{
    int status;
    String code;
    String message;
}
