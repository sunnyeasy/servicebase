package com.easy.common.network;

public enum ServerPorts {
    gamePushMqttTcpPort(16688),
    gamePushMqttTcpClusterPort(16689),

    userHttpPort(16100),
    userMotanPort(16101),

    userTestHttpPort(16110);

    private ServerPorts(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private int port;
}
