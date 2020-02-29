package com.easy.push.rpcvo;

import com.easy.common.rpcvo.BaseRpcVo;

public class PushServerRpcVo extends BaseRpcVo {
    private static final long serialVersionUID = -1637438423310546345L;

    private String hostname;
    private int port;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
