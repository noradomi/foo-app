package vn.zalopay.phucvt.fooapp.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtConfig {
    int expireToken = 174600; // seconds
}
