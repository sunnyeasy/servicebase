package com.easy.terminal.client;

import java.io.Serializable;

public class SignInVo implements Serializable {
    private static final long serialVersionUID = -3133125328090527306L;

    private long uid;
    private String token;
    private String nickName;
    private String headPictureUrl;
    private int realNameStatus;

    private PushServer pushServer;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public int getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(int realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public PushServer getPushServer() {
        return pushServer;
    }

    public void setPushServer(PushServer pushServer) {
        this.pushServer = pushServer;
    }
}
