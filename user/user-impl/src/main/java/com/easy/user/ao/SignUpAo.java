package com.easy.user.ao;

import com.easy.constant.enums.AgentMode;
import com.easy.constant.enums.BusinessType;

import java.io.Serializable;

public class SignUpAo implements Serializable {
    private static final long serialVersionUID = -4894216518892888057L;

    private String mobile;
    private String lon;
    private String lat;
    private String cityCode;
    private String deviceId;
    private String referredMobile;
    private String version;
    private String captcha;
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getReferredMobile() {
        return referredMobile;
    }

    public void setReferredMobile(String referredMobile) {
        this.referredMobile = referredMobile;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
