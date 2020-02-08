package com.easy.common.network.packet.push;

import java.io.Serializable;

public class RpcPushRequest implements Serializable {
    private static final long serialVersionUID = -8379288857206109521L;

    private long uid;
    private String data;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
