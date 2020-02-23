package com.easy.push.cluster;

import com.easy.common.network.ServerPorts;
import com.easy.common.network.packet.push.PushMessage;
import com.easy.common.network.packet.push.RpcPushRequest;
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
        pushNode.setClusterPort(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        PushMqttClient client=new PushMqttClient(pushNode);

        PushMessage message = new PushMessage();
        message.setUid(1000000L);
        message.setData("hello easyfun");

        client.publishMessage(message);
    }
}
