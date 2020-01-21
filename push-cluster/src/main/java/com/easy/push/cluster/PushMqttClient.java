package com.easy.push.cluster;

import com.alibaba.fastjson.JSON;
import com.easy.common.network.NetworkConstants;
import com.easy.common.network.packet.RpcPushRequest;
import com.easy.push.registry.zookeeper.PushNode;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public class PushMqttClient implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PushMqttClient.class);

    private PushNode pushNode;

    private boolean isWaiting = false;
    private Object wait;
    private Queue<RpcPushRequest> queue;
    private Thread thread;
    private MqttAsyncClient client;

    public PushMqttClient(PushNode pushNode, ThreadFactory threadFactory) {
        this.wait = new Object();
        this.pushNode = pushNode;
        this.queue = new LinkedBlockingQueue<>();
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

        client.publish(NetworkConstants.CLUSTER_PUSH_TOPIC, message);

        queue.poll();
    }

    private boolean connect() {
        return true;
    }
}
