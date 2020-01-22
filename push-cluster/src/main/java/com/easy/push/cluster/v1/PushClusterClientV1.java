package com.easy.push.cluster.v1;

import com.easy.common.network.packet.RpcPushRequest;
import com.easy.push.cluster.PushMqttClient;
import com.easy.push.registry.zookeeper.PushNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该实现暂时有缺陷
 * */
public class PushClusterClientV1 {
    private static final Logger logger = LoggerFactory.getLogger(PushClusterClientV1.class);

//    private StandardThreadFactory threadFactory;
    private Map<String, PushMqttClient> mqttClientMap;

    public PushClusterClientV1() {
//        this.threadFactory = new StandardThreadFactory("pushClusterClient", true);
        this.mqttClientMap = new ConcurrentHashMap<>();
    }

    public void publishMessage(PushNode pushNode, RpcPushRequest request) {
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
        mqttClient.publishMessage(request);
    }
}
