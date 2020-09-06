package vn.zalopay.phucvt.fooapp.entity.response.data;

import lombok.Builder;

@Builder
public class LoginDataResponse {
    String token;
    String userId;
}
