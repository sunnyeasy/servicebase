package com.easy.push.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.easy.push.transport.netty4.MqttConfig;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterZookeeperRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ClusterZookeeperRegistry.class);

    private static final String PATH_CONNECTOR = "/";
    private String clusterPath;
    private MqttConfig mqttConfig;
    private ZkClient zkClient;
    private Map<String, PushNode> pushNodeMap = new ConcurrentHashMap<>();

    public ClusterZookeeperRegistry(ZkClient zkClient, MqttConfig mqttConfig, String clusterPath) {
        this.clusterPath = clusterPath;
        this.mqttConfig = mqttConfig;
        this.zkClient = zkClient;

        IZkStateListener zkStateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                //do nothing
            }

            @Override
            public void handleNewSession() throws Exception {
                logger.info("zkRegistry get new session notify.");
                createPushNode();
            }
        };
        zkClient.subscribeStateChanges(zkStateListener);
    }

    public void register() {
        if (!zkClient.exists(clusterPath)) {
            zkClient.createPersistent(clusterPath, true);
        }
        IZkChildListener zkChildListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                logger.info("parentPath={}, currentChilds={}", parentPath, JSON.toJSONString(currentChilds));
                flushPushNode(parentPath, currentChilds);
            }
        };
        zkClient.subscribeChildChanges(clusterPath, zkChildListener);

        createPushNode();
    }

    private void createPushNode() {
        String nodePath = clusterPath + PATH_CONNECTOR + mqttConfig.getHostname();
        if (zkClient.exists(nodePath)) {
            zkClient.delete(nodePath);
        }

        PushNode node = new PushNode();
        node.setHostname(mqttConfig.getHostname());
        node.setClusterPort(mqttConfig.getPort());
        zkClient.createEphemeral(nodePath, JSON.toJSONString(node));
    }

    private synchronized void flushPushNode(String parentPath, List<String> currentChilds) {
        Set<String> childs = pushNodeMap.keySet();
        for (String child : childs) {
            if (!currentChilds.contains(child)) {
                pushNodeMap.remove(child);
            }
        }

        for (String child : currentChilds) {
            flushPushNode(parentPath, child);
        }
    }

    private synchronized void flushPushNode(String parentPath, String currentChild) {
        String nodePath = parentPath + PATH_CONNECTOR + currentChild;
        String data = zkClient.readData(nodePath);
        PushNode pushNode = JSON.parseObject(data, PushNode.class);
        pushNodeMap.put(pushNode.getHostname(), pushNode);
    }
}
