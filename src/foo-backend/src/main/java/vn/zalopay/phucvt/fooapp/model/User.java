package vn.zalopay.phucvt.fooapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private String fullName;

}
