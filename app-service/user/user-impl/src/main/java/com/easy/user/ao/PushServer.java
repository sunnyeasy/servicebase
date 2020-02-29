package com.easy.user.ao;

import java.io.Serializable;

public class PushServer implements Serializable {
    private static final long serialVersionUID = -585423781696834760L;

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
