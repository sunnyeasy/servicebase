package com.easy.game.gateway;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.ServerPorts;
import com.easy.gateway.transport.netty4.GatewayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class GameGatewayConfig {
    private static final Logger logger = LoggerFactory.getLogger(GameGatewayServer.class);

    @Autowired
    private Environment environment;

    @Bean
    public GatewayConfig gatewayConfig() {
        GatewayConfig config = new GatewayConfig();

        String defaultValue = String.valueOf(ServerPorts.gameGatewayHttpPort.getPort());
        String value = environment.getProperty("game.gateway.http.port", defaultValue);
        int port = Integer.valueOf(value);
        config.setPort(port);

        logger.info("Game gateway config: {}", JSON.toJSONString(config));
        return config;
    }
}
