package com.easy.push.cluster;

import com.easy.common.transport.ServerPorts;
import com.easy.common.transport.packet.push.PushMessage;
import com.easy.push.registry.zookeeper.PushNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushClusterClientTest {
    private static final Logger logger = LoggerFactory.getLogger(PushClusterClientTest.class);

    @Test
    public void pushMessage() {
        PushNode pushNode = new PushNode();
        pushNode.setHostname("127.0.0.1");
        pushNode.setClusterPort(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        PushClusterClient client = new PushClusterClient();

        PushMessage message = new PushMessage();
        message.setUid(1000000L);
        message.setData("hello easyfun");

        client.push(pushNode, message);

    }
}
