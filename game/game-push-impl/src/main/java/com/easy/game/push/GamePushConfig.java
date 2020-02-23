package com.easy.game.push;

import com.alibaba.fastjson.JSON;
import com.easy.common.network.ServerPorts;
import com.easy.common.redis.RedisClient;
import com.easy.common.redis.RedisClientFactory;
import com.easy.common.redis.RedisConfig;
import com.easy.push.cluster.PushClusterClient;
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

        String defaultValue = String.valueOf(ServerPorts.gamePushMqttTcpPort.getPort());
        String value = environment.getProperty("mqtt.tcp.port", defaultValue);
        int port = Integer.valueOf(value);
        config.setPort(port);

        logger.info("Mqtt tcp config: {}", JSON.toJSONString(config));
        return config;
    }

    @Bean("mqttTcpClusterConfig")
    public MqttConfig mqttTcpClusterConfig() {
        MqttConfig config = new MqttConfig();

        String defaultValue = String.valueOf(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        String value = environment.getProperty("mqtt.tcp.cluster.port", defaultValue);
        int port = Integer.valueOf(value);
        config.setPort(port);

        logger.info("Mqtt tcp cluster config: {}", JSON.toJSONString(config));
        return config;
    }

    @Bean
    public RedisConfig redisConfig() {
        RedisConfig config = RedisConfig.build(environment);
        logger.info("redisConfig={}", JSON.toJSON(config));
        return config;
    }

    @Bean
    public RedisClient redisClient(@Autowired RedisConfig redisConfig) {
        RedisClient client = RedisClientFactory.create("gamePush", redisConfig);
        return client;
    }

    @Bean
    public PushClusterClient pushClusterClient() {
        PushClusterClient client=new PushClusterClient();
        return client;
    }
}
