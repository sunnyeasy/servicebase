package com.easy.terminal.client;

import com.easy.constant.enums.user.model.SignInLogEnums.SignInType;

import java.io.Serializable;

public class SignInAo implements Serializable {
    private static final long serialVersionUID = 3810197754674167888L;

    private SignInType signInType;
    private String mobile;
    private String password;
    private String lon;
    private String lat;
    private String cityCode;
    private String deviceId;

    public SignInType getSignInType() {
        return signInType;
    }

    public void setSignInType(SignInType signInType) {
        this.signInType = signInType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
