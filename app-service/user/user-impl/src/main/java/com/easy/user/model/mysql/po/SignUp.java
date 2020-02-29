package com.easy.user.model.mysql.po;

import java.util.Date;

public class SignUp {
    private Long uid;

    private String businessType;

    private String agentMode;

    private String ip;

    private String lon;

    private String lat;

    private String cityCode;

    private String deviceId;

    private Long referrerUid;

    private Long signUpId;

    private Date signUpTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(String agentMode) {
        this.agentMode = agentMode == null ? null : agentMode.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon == null ? null : lon.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Long getReferrerUid() {
        return referrerUid;
    }

    public void setReferrerUid(Long referrerUid) {
        this.referrerUid = referrerUid;
    }

    public Long getSignUpId() {
        return signUpId;
    }

    public void setSignUpId(Long signUpId) {
        this.signUpId = signUpId;
    }

    public Date getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Date signUpTime) {
        this.signUpTime = signUpTime;
    }
}