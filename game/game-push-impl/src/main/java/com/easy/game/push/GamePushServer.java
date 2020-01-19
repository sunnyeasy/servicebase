package com.easy.game.push;

import com.easy.push.transport.netty4.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class GamePushServer {
    private static final Logger logger = LoggerFactory.getLogger(GamePushServer.class);

    @Autowired
    @Qualifier("mqttTcpConfig")
    private MqttConfig mqttTcpConfig;

    @Autowired
    @Qualifier("mqttTcpClusterConfig")
    private MqttConfig mqttTcpClusterConfig;

    @Autowired
    private MqttListener mqttListener;

    @Autowired
    private MqttClusterListener mqttClusterListener;

    private ApplicationContext applicationContext;

    private MqttRouter mqttRouter;
    private MqttTcpServer mqttTcpServer;

    private MqttClusterRouter mqttClusterRouter;
    private MqttTcpClusterServer mqttTcpClusterServer;

    public synchronized void init(ApplicationContext context) {
        this.applicationContext = context;

        if (null == mqttTcpServer) {
            mqttRouter = new MqttRouter(mqttTcpConfig, mqttListener);
            mqttTcpServer = new MqttTcpServer(mqttTcpConfig, null, mqttRouter);
            mqttTcpServer.open();
        }

        if (null == mqttTcpClusterServer) {
            mqttClusterRouter = new MqttClusterRouter(mqttRouter, mqttClusterListener);
            mqttTcpClusterServer = new MqttTcpClusterServer(mqttTcpClusterConfig, mqttClusterRouter);
            mqttTcpClusterServer.open();
        }
    }
}
