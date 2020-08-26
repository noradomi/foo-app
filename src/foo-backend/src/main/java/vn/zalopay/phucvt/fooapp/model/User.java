package vn.zalopay.phucvt.fooapp.model;


import java.io.Serializable;


public class User implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private String fullName;

    public User(String userId, String userName, String password, String fullName) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
