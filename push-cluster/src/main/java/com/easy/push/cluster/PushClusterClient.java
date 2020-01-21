package com.easy.push.cluster;

import com.easy.common.network.packet.RpcPushRequest;
import com.easy.common.thread.StandardThreadFactory;
import com.easy.push.registry.zookeeper.ClusterZookeeperRegistry;
import com.easy.push.registry.zookeeper.PushNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PushClusterClient {
    private static final Logger logger = LoggerFactory.getLogger(PushClusterClient.class);

    private StandardThreadFactory threadFactory;
    private Map<String, PushMqttClient> mqttClientMap;

    public PushClusterClient() {
        this.threadFactory = new StandardThreadFactory("pushClusterClient", true);
        this.mqttClientMap = new ConcurrentHashMap<>();
    }

    public void publishMessage(PushNode pushNode, RpcPushRequest request) {
        PushMqttClient mqttClient = mqttClientMap.get(pushNode.getHostname());
        if (null == mqttClient) {
            synchronized (this) {
                mqttClient = mqttClientMap.get(pushNode.getHostname());
                if (null == mqttClient) {
                    mqttClient = new PushMqttClient(pushNode, threadFactory);
                    mqttClientMap.put(pushNode.getHostname(), mqttClient);
                }
            }
        }
        mqttClient.publishMessage(request);
    }
}
