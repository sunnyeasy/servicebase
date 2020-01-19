package com.easy.game.push;

import com.alibaba.fastjson.JSON;
import com.easy.push.transport.netty4.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class GamePushConfig {
    private static final Logger logger = LoggerFactory.getLogger(GamePushConfig.class);

    @Autowired
    private Environment environment;

    @Bean("mqttTcpConfig")
    public MqttConfig mqttTcpConfig() {
        MqttConfig config = new MqttConfig();

        String value = environment.getProperty("mqtt.tcp.port", "16688");
        int port = Integer.valueOf(value);
        config.setPort(port);

        logger.info("Mqtt tcp config: {}", JSON.toJSONString(config));
        return config;
    }

    @Bean("mqttTcpClusterConfig")
    public MqttConfig mqttTcpClusterConfig() {
        MqttConfig config = new MqttConfig();

        String value = environment.getProperty("mqtt.tcp.cluster.port", "16689");
        int port = Integer.valueOf(value);
        config.setPort(port);

        logger.info("Mqtt tcp cluster config: {}", JSON.toJSONString(config));
        return config;
    }
}
