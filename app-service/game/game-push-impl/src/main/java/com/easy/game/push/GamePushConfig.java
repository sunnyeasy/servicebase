package com.easy.game.push;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.ServerPorts;
import com.easy.common.redis.RedisClient;
import com.easy.common.redis.RedisClientFactory;
import com.easy.common.redis.RedisConfig;
import com.easy.common.zookeeper.ZkConfig;
import com.easy.common.zookeeper.ZkConfigFactory;
import com.easy.push.cluster.PushClusterClient;
import com.easy.push.cluster.PushClusterConstant;
import com.easy.push.registry.zookeeper.ClusterZookeeperRegistry;
import com.easy.push.registry.zookeeper.PushNode;
import com.easy.push.transport.netty4.MqttConfig;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Date;

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
        PushClusterClient client = new PushClusterClient();
        return client;
    }

    @Bean
    public PushNode pushNode() {
        PushNode node = new PushNode();

        String v = environment.getProperty("pushNode.hostname");
        node.setHostname(v);
        node.setClusterPort(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        node.setPort(ServerPorts.gamePushMqttTcpPort.getPort());
        node.setCreateTime(new Date());
        return node;
    }

    @Bean
    public ZkConfig zkConfig() {
        return ZkConfigFactory.build(environment);
    }

    @Bean
    public ClusterZookeeperRegistry clusterZookeeperRegistry(@Autowired ZkConfig zkConfig, @Autowired PushNode pushNode) {
        ZkClient zkClient = new ZkClient(zkConfig.getAddress(), zkConfig.getSessionTimeout(), zkConfig.getConnectTimeout());
        ClusterZookeeperRegistry registry = new ClusterZookeeperRegistry(zkClient, pushNode, PushClusterConstant.PUSH_CLUSTER_NODE_PATH);
        registry.register();
        return registry;
    }
}
