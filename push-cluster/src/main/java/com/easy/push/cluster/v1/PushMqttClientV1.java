package com.easy.push.cluster.v1;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.NetworkConstants;
import com.easy.common.transport.packet.push.RpcPushRequest;
import com.easy.push.registry.zookeeper.PushNode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 该实现暂时有缺陷
 * 1.ack暂未区别
 */
public class PushMqttClientV1 implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PushMqttClientV1.class);

    private PushNode pushNode;

    private boolean isWaiting = false;
    private Object wait;
    private Queue<RpcPushRequest> queue;
    private Thread thread;

    private Object clientLock;
    private Object clientConnectLock;
    private MqttAsyncClient client;

    public PushMqttClientV1(PushNode pushNode, ThreadFactory threadFactory) {
        this.wait = new Object();
        this.pushNode = pushNode;
        this.queue = new LinkedBlockingQueue<>();
        this.clientLock = new Object();
        this.clientConnectLock = new Object();
        this.thread = threadFactory.newThread(this::run);
        this.thread.start();
    }

    public void publishMessage(RpcPushRequest message) {
        queue.add(message);
        wakeUp();
    }

    private void wakeUp() {
        synchronized (wait) {
            if (isWaiting) {
                isWaiting = false;
                wait.notify();
            }
        }
    }

    private void idle() throws InterruptedException {
        if (!queue.isEmpty()) {
            return;
        }

        synchronized (wait) {
            if (!isWaiting) {
                isWaiting = true;
                wait.wait();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                doing();
            } catch (Exception e) {
                logger.error("Cluster publish push message exception. pushNode={}", JSON.toJSONString(pushNode), e);
                // TODO:失败剔除
            }
        }
    }

    private void doing() throws InterruptedException, MqttException {
        if (!connect()) {
            logger.info("Connect to cluster server fail. pushNode={}", JSON.toJSONString(pushNode));
            Thread.sleep(5000);
            return;
        }

        RpcPushRequest request = queue.peek();
        if (null == request) {
            idle();
            return;
        }

        MqttMessage message = new MqttMessage(JSON.toJSONString(request).getBytes());
        message.setQos(MqttQoS.AT_LEAST_ONCE.value());
        message.setRetained(false);

        IMqttDeliveryToken token = client.publish(NetworkConstants.CLUSTER_PUSH_TOPIC, message);

        queue.poll();
    }

    private boolean connect() {
        if (null == client) {
            synchronized (clientLock) {
                if (null == client) {
                    String serverURI = "tcp://" + pushNode.getHostname() + ":" + pushNode.getClusterPort();
                    String clientId = UUID.randomUUID().toString();
                    MemoryPersistence persistence = new MemoryPersistence();

                    try {
                        client = new MqttAsyncClient(serverURI, clientId, persistence);
                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable throwable) {
                                logger.info("Disconnect");
                            }

                            @Override
                            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                                logger.info("topic={}", s);
                                logger.info("qoS={}", mqttMessage.getQos());
                                logger.info("payload={}", new String(mqttMessage.getPayload(), NetworkConstants.UTF8));
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                                logger.info("delivery complete, isComplete={}", iMqttDeliveryToken.isComplete());
                                logger.info("packetId={}", iMqttDeliveryToken.getMessageId());
                            }
                        });
                    } catch (MqttException e) {
                        logger.error("Create MqttAsyncClient exception, pushNode={}", JSON.toJSONString(pushNode), e);
                        return false;
                    }
                }
            }
        }

        if (client.isConnected()) {
            return true;
        }

        synchronized (clientConnectLock) {
            if (client.isConnected()) {
                return true;
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setKeepAliveInterval(20);
            options.setConnectionTimeout(10);
            options.setMaxInflight(10000);
            options.setMqttVersion(MqttVersion.MQTT_3_1_1.protocolLevel());
            options.setUserName("easyfun");
            options.setPassword("easyfun".toCharArray());

            try {
                client.connect(options);
            } catch (MqttException e) {
                logger.error("Connect to cluster server fail, pushNode={}", JSON.toJSONString(pushNode), e);
                return false;
            }
        }
        return client.isConnected();
    }
}
