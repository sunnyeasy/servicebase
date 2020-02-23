package com.easy.common.network;

public enum ServerPorts {
    gamePushMqttTcpPort(15000),
    gamePushMqttTcpClusterPort(15001),

    gamePushHttpPort(15100),
    gamePushMotanPort(15101),

    gamePushTestHttpPort(15110),

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
