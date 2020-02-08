package com.easy.push.cluster;

import com.easy.common.network.packet.push.RpcPushRequest;
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
        pushNode.setClusterPort(16689);
        PushClusterClient client = new PushClusterClient();

        RpcPushRequest request = new RpcPushRequest();
        request.setUid(1000000L);
        request.setData("hello easyfun");

        client.push(pushNode, request);

    }
}
