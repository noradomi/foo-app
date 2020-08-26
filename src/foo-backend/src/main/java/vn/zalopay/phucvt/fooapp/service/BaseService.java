package vn.zalopay.phucvt.fooapp.service;

import vn.zalopay.phucvt.fooapp.manager.JWTManager;

public class BaseService {
    public static final String AUTHORIZATION_SCHEMA = "Bearer";

    protected JWTManager jwtManager;

    public JWTManager getJwtManager() {
        return jwtManager;
    }

    public void setJwtManager(JWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }
}
