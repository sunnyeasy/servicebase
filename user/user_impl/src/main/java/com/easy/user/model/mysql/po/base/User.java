package com.easy.user.model.mysql.po.base;

import java.util.Date;

public class User {
    private Long uid;

    private String mobile;

    private String mobileStatus;

    private String email;

    private String emailStatus;

    private String realNameStatus;

    private String password;

    private String userStatus;

    private String nickName;

    private String headPictureUrl;

    private String signUpMode;

    private Date createTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus == null ? null : mobileStatus.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus == null ? null : emailStatus.trim();
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus == null ? null : realNameStatus.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl == null ? null : headPictureUrl.trim();
    }

    public String getSignUpMode() {
        return signUpMode;
    }

    public void setSignUpMode(String signUpMode) {
        this.signUpMode = signUpMode == null ? null : signUpMode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}