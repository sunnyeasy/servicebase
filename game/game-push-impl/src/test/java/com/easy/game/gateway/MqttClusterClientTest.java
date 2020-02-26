package com.easy.game.gateway;

import com.alibaba.fastjson.JSON;
import com.easy.common.container.Main;
import com.easy.common.transport.NetworkConstants;
import com.easy.common.transport.ServerPorts;
import com.easy.common.transport.packet.push.PushMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClusterClientTest {
    private static final Logger logger = LoggerFactory.getLogger(MqttClusterClientTest.class);

    @Test
    public void connect() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient("tcp://localhost:" + ServerPorts.gamePushMqttTcpClusterPort.getPort(), String.valueOf(System.currentTimeMillis()), persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName("easyfun");
            options.setPassword("easyfun".toCharArray());
            options.setKeepAliveInterval(20);

            mqttClient.setCallback(new MqttCallback() {
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

            mqttClient.connect(options);

            PushMessage pushMessage = new PushMessage();
            pushMessage.setUid(1000000L);
            pushMessage.setData("hello easyfun");
            pushMessage.setTopic("/test/client/to/server");

            MqttMessage message = new MqttMessage(JSON.toJSONString(pushMessage).getBytes());
            message.setQos(MqttQoS.AT_LEAST_ONCE.value());

            mqttClient.publish(NetworkConstants.CLUSTER_PUSH_TOPIC, message);

//            message = new MqttMessage(JSON.toJSONString(pushMessage).getBytes());
//            message.setQos(MqttQoS.AT_LEAST_ONCE.value());
//            mqttClient.publish(NetworkConstants.CLUSTER_PUSH_TOPIC, message);

        } catch (Exception e) {
            logger.error("请求失败, ", e);
        }

        Main.await("Mqtt client");
    }
}
