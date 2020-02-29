package com.easy.push.cluster;

import com.easy.common.transport.packet.push.PushMessage;
import com.easy.push.registry.zookeeper.PushNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PushClusterClient {
    private static final Logger logger = LoggerFactory.getLogger(PushClusterClient.class);

    private Map<String, PushMqttClient> mqttClientMap;

    public PushClusterClient() {
        this.mqttClientMap = new ConcurrentHashMap<>();
    }

    public void push(PushNode pushNode, PushMessage message) {
        PushMqttClient mqttClient = mqttClientMap.get(pushNode.getHostname());
        if (null == mqttClient) {
            synchronized (this) {
                mqttClient = mqttClientMap.get(pushNode.getHostname());
                if (null == mqttClient) {
                    mqttClient = new PushMqttClient(pushNode);
                    mqttClientMap.put(pushNode.getHostname(), mqttClient);
                }
            }
        }
        mqttClient.publishMessage(message);
    }
}
