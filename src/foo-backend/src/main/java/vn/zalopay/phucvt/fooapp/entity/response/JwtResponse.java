package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

@Builder
public class JwtResponse {
    String token;
    String userId;
}
