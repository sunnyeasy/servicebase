package com.easy.game.gateway;

import com.easy.gateway.transport.netty4.GatewayConfig;
import com.easy.gateway.transport.netty4.GatewayListener;
import com.easy.gateway.transport.netty4.GatewayServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class GameGatewayServer {
    private static final Logger logger = LoggerFactory.getLogger(GameGatewayServer.class);

    private ApplicationContext applicationContext;
    private GatewayServer gatewayServer;

    @Autowired
    private GatewayConfig gatewayConfig;
    @Autowired
    private GatewayListener gatewayListener;

    public void init(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        if (null == gatewayServer) {
            gatewayServer = new GatewayServer(gatewayConfig, gatewayListener);
            gatewayServer.open();
        }
    }
}
