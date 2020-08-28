package vn.zalopay.phucvt.fooapp.entity.response;

import lombok.Builder;

public abstract class BaseResponse {
    protected int status;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
