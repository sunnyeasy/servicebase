package com.easy.gateway.transport.netty4;

import com.easy.gateway.transport.netty4.message.MqttAuthMessage;
import io.netty.handler.codec.mqtt.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(MqttRouter.class);

    private MqttListener mqttListener;

    public MqttRouter(MqttListener mqttListener) {
        this.mqttListener = mqttListener;
    }

    public void processMessage(MqttChannel channel, MqttMessage message) throws Exception {
        MqttMessageType messageType = message.fixedHeader().messageType();
        if (MqttMessageType.CONNECT == messageType) {
            processConnect(channel, (MqttConnectMessage) message);
            return;
        }

        if (!channel.isAuth()) {
            logger.error("Channel has not authed, uid={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            channel.close();
            return;
        }

        switch (messageType) {
            case DISCONNECT:
                logger.info("Client close channel, uid={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                processDisConnect(channel);
                break;
        }
    }

    private void processConnect(MqttChannel channel, MqttConnectMessage message) throws Exception {
        MqttConnectReturnCode returnCode = MqttConnectReturnCode.CONNECTION_ACCEPTED;

        String clientId;
        for (int i = 0; i < 1; i++) {
            clientId = message.payload().clientIdentifier();
            if (StringUtils.isBlank(clientId)) {
                logger.error("Uid is null");
                returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;
                break;
            }

            channel.setClientId(clientId);

            int version = message.variableHeader().version();
            if (version != MqttVersion.MQTT_3_1.protocolLevel() && version != MqttVersion.MQTT_3_1_1.protocolLevel()) {
                logger.error("Mqtt protocol version not supportted. Only support mqtt_3.1,mqtt_3.1.1. uid={}", clientId);
                returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION;
                break;
            }

            if (!message.variableHeader().hasUserName() || !message.variableHeader().hasPassword()) {
                logger.error("Username and password cannot null. uid={}", clientId);
                returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD;
                break;
            }
        }

        //连接认证失败
        if (returnCode != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
            MqttConnAckMessage ackMessage = MqttAckMessageFactory.mqttConnAckMessage(returnCode, false);
            channel.writeAndFlush(ackMessage);
            channel.close();
            return;
        }

        //连接认证
        MqttAuthMessage authMessage = new MqttAuthMessage(channel, message.payload().userName(), message.payload().password());
        mqttListener.auth(authMessage, this);
    }

    public void processDisConnect(MqttChannel channel) throws Exception {

    }

    @Override
    public void auth(MqttChannel channel) {
        if (!channel.getChannel().isActive()) {
            logger.error("Channel has closed, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return;
        }

        //认证失败
        if (!channel.isAuth()) {
            logger.error("Channel auth fail, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            MqttConnAckMessage ackMessage = MqttAckMessageFactory.mqttConnAckMessage(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false);

            try {
                channel.writeAndFlush(ackMessage);
            } catch (Exception e) {
                logger.error("Send refused MqttConnAckMessage exception, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
            }
            channel.close();
            return;
        }

        //认证成功
        MqttConnAckMessage ackMessage = MqttAckMessageFactory.mqttConnAckMessage(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
        try {
            channel.writeAndFlush(ackMessage);
        } catch (Exception e) {
            logger.error("Send accepted MqttConnAckMessage exception, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
            channel.close();
            return;
        }

        //自动订阅topic
        String subTopic = mqttListener.subscribeTopic(channel.getClientId());
        if (StringUtils.isNotBlank(subTopic)) {
        }

        //通道握手成功
    }
}
