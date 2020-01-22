package com.easy.push.cluster;

import com.easy.common.network.packet.RpcPushRequest;
import com.easy.push.registry.zookeeper.PushNode;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushMqttClientTest {
    private static final Logger logger= LoggerFactory.getLogger(PushMqttClientTest.class);

    @Test
    public void publishMessage() throws MqttException {
        PushNode pushNode=new PushNode();
        pushNode.setHostname("127.0.0.1");
        pushNode.setClusterPort(16689);
        PushMqttClient client=new PushMqttClient(pushNode);

        RpcPushRequest request = new RpcPushRequest();
        request.setUid(1000000L);
        request.setData("hello easyfun");

        client.publishMessage(request);
    }
}
