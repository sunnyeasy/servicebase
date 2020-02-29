package com.easy.common.transport;

public enum ServerPorts {
    //游戏推送
    gamePushMqttTcpPort(15000),
    gamePushMqttTcpClusterPort(15001),

    gamePushMotanPort(15100),
    gamePushTomcatHttpPort(15110),

    gamePushTestTomcatHttpPort(15190),

    //游戏网关
    gameGatewayHttpPort(15200),
    gameGatewayTomcatHttpPort(15210),

    //用户
    userTomcatHttpPort(18100),
    userMotanPort(18101),

    userTestTomcatHttpPort(18120),

    //支付
    payTomcatHttpPort(18130),
    payMotanPort(18131),

    payTestTomcatHttpPort(18240);

    private ServerPorts(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private int port;
}
