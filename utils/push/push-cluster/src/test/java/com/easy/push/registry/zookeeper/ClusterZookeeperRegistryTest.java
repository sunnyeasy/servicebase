package com.easy.push.registry.zookeeper;

import com.easy.common.container.Main;
import com.easy.common.transport.ServerPorts;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterZookeeperRegistryTest {
    private static final Logger logger = LoggerFactory.getLogger(ClusterZookeeperRegistryTest.class);

    private String clusterPath="/test/push/cluster";

    @Test
    public void test() {
        String address = "172.16.51.1:2181,172.16.51.2:2181,172.16.51.3:2181";
        ZkClient zkClient = new ZkClient(address, 60000, 20000);

        PushNode pushNode = new PushNode();
        pushNode.setHostname("127.0.0.1");
        pushNode.setPort(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        ClusterZookeeperRegistry registry = new ClusterZookeeperRegistry(zkClient, pushNode, clusterPath);

        registry.register();

        Main.await("clusterZookeeperRegistryTest");
    }

    @Test
    public void test2() {
        String address = "172.16.51.1:2181,172.16.51.2:2181,172.16.51.3:2181";
        ZkClient zkClient = new ZkClient(address, 60000, 20000);

        PushNode pushNode = new PushNode();
        pushNode.setHostname("127.0.0.2");
        pushNode.setPort(ServerPorts.gamePushMqttTcpClusterPort.getPort());
        ClusterZookeeperRegistry registry = new ClusterZookeeperRegistry(zkClient, pushNode, clusterPath);

        registry.register();

        Main.await("clusterZookeeperRegistryTest");
    }

}
