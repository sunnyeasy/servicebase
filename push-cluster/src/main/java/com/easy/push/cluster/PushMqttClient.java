package com.easy.push.cluster;

import com.alibaba.fastjson.JSON;
import com.easy.common.exception.BusinessException;
import com.easy.common.network.NetworkConstants;
import com.easy.common.network.packet.push.PushMessage;
import com.easy.common.network.packet.push.RpcPushRequest;
import com.easy.push.PushClusterResponseCode;
import com.easy.push.registry.zookeeper.PushNode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PushMqttClient {
    private static final Logger logger = LoggerFactory.getLogger(PushMqttClient.class);

    private static final int KEEP_ALIVE_INTERVAL = 20;
    private static final int CONNECTION_TIME_OUT = 10;
    private static final int MAX_IN_FLIGHT = 10000;
    private static final int CONNECT_WAIT_FOR_COMPLETION = 3000;
    private static final int PUBLISH_WAIT_FOR_COMPLETION = 3000;

    private PushNode pushNode;

    private Object clientLock;
    private Object clientConnectLock;
    private MqttAsyncClient client;

    public PushMqttClient(PushNode pushNode) {
        this.pushNode = pushNode;
        this.clientLock = new Object();
        this.clientConnectLock = new Object();
    }

    public void publishMessage(PushMessage message) {
        if (!connect()) {
            logger.info("Connect to cluster server fail. pushNode={}", JSON.toJSONString(pushNode));
            throw new BusinessException(PushClusterResponseCode.CONNECT_TO_CLUSTER_SERVER_FAIL);
        }

        MqttMessage mqttMessage = new MqttMessage(JSON.toJSONString(message).getBytes());
        mqttMessage.setQos(MqttQoS.AT_LEAST_ONCE.value());
        mqttMessage.setRetained(false);

        IMqttDeliveryToken token;
        try {
            token = client.publish(NetworkConstants.CLUSTER_PUSH_TOPIC, mqttMessage);
            token.waitForCompletion(PUBLISH_WAIT_FOR_COMPLETION);
        } catch (MqttException e) {
            logger.error("Publish message to cluster server exception. pushNode={}", JSON.toJSONString(pushNode), e);
            throw new BusinessException(PushClusterResponseCode.PUBLISH_MESSAGE_TO_CLUSTER_SERVER_FAIL);
        }

        if (null != token.getException()) {
            logger.error("Publish message to cluster server exception. pushNode={}", JSON.toJSONString(pushNode), token.getException());
            throw new BusinessException(PushClusterResponseCode.PUBLISH_MESSAGE_TO_CLUSTER_SERVER_FAIL);
        }
    }

    private boolean connect() {
        if (!createClient()) {
            return false;
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
            options.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
            options.setConnectionTimeout(CONNECTION_TIME_OUT);
            options.setMaxInflight(MAX_IN_FLIGHT);
            options.setMqttVersion(MqttVersion.MQTT_3_1_1.protocolLevel());
            options.setUserName("easyfun");
            options.setPassword("easyfun".toCharArray());

            try {
                IMqttToken token = client.connect(options);
                token.waitForCompletion(CONNECT_WAIT_FOR_COMPLETION);
                if (null != token.getException()) {
                    logger.error("Connect to cluster server fail, pushNode={}", JSON.toJSONString(pushNode), token.getException());
                    return false;
                }
            } catch (MqttException e) {
                logger.error("Connect to cluster server fail, pushNode={}", JSON.toJSONString(pushNode), e);
                return false;
            }
        }
        return client.isConnected();
    }

    private boolean createClient() {
        if (null != client) {
            return true;
        }

        synchronized (clientLock) {
            if (null != client) {
                return true;
            }

            String serverURI = "tcp://" + pushNode.getHostname() + ":" + pushNode.getClusterPort();
            String clientId = UUID.randomUUID().toString();
            MemoryPersistence persistence = new MemoryPersistence();

            try {
                client = new MqttAsyncClient(serverURI, clientId, persistence);
            } catch (MqttException e) {
                logger.error("Create MqttAsyncClient exception, pushNode={}", JSON.toJSONString(pushNode), e);
                return false;
            }

//            client.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable throwable) {
//                    logger.info("Disconnect");
//                }
//
//                @Override
//                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                    logger.info("topic={}", s);
//                    logger.info("qoS={}", mqttMessage.getQos());
//                    logger.info("payload={}", new String(mqttMessage.getPayload(), NetworkConstants.UTF8));
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                    logger.info("delivery complete, isComplete={}", iMqttDeliveryToken.isComplete());
//                    logger.info("packetId={}", iMqttDeliveryToken.getMessageId());
//                }
//            });
            return true;
        }
    }
}
