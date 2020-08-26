package vn.zalopay.phucvt.fooapp.util;

public enum HttpStatus {
    OK(200,"OK"),
    BAD_REQUEST(400,"Bad Request"),
    UNTHORIZED(401,"Unthorized");

    private int code;
    private String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code(){
        return code;
    }
    
    public String message(){
        return message;
    }
}
