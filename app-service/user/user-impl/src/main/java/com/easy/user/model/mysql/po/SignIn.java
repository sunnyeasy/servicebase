package com.easy.user.model.mysql.po;

import java.util.Date;

public class SignIn extends SignInKey {
    private String deviceId;

    private String token;

    private Long signInId;

    private Date authTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Long getSignInId() {
        return signInId;
    }

    public void setSignInId(Long signInId) {
        this.signInId = signInId;
    }

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }
}