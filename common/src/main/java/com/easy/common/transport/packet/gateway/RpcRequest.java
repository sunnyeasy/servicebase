package com.easy.common.transport.packet.gateway;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 7771833959789923885L;

    private long uid;
    private String url;
    private String data;
    private String params;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
