package com.easy.push.transport.netty4;

import com.easy.common.network.NetworkConstants;
import com.easy.common.network.packet.push.PushMessage;
import io.netty.handler.codec.mqtt.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注意:
 * 面向集群间消息发送
 * 1.只用到MQTT协议的部分命令
 * CONNECT
 * DISCONNECT
 * PUBLISH
 * PINGREQ
 * <p>
 * 2.QoS只支持at least once
 */
public class MqttClusterRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(MqttClusterRouter.class);

    private PushRouter pushRouter;
    private MqttClusterListener listener;

    private Map<String, MqttChannel> channelMap;

    public MqttClusterRouter(PushRouter pushRouter, MqttClusterListener mqttClusterListener) {
        this.pushRouter = pushRouter;
        this.listener = mqttClusterListener;
        this.channelMap = new ConcurrentHashMap<>();
    }

    @Override
    public void processMessage(MqttChannel channel, MqttMessage message) throws Exception {
        MqttMessageType messageType = message.fixedHeader().messageType();
        if (MqttMessageType.CONNECT == messageType) {
            processConnect(channel, (MqttConnectMessage) message);
            return;
        }

        if (!channel.isAuth()) {
            logger.error("Cluster channel has not authed, close channel, uid={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            channel.close();
            return;
        }

        switch (messageType) {
            case DISCONNECT:
                logger.info("Client close cluster channel, clientId={}, remote={}, local={}",
                        channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                processDisConnect(channel);
                break;

            case PINGREQ:
                processPingReq(channel);
                break;

            case PUBLISH:
                processPublish(channel, (MqttPublishMessage) message);
                break;

            default:
                logger.info("Message type has not been supported, close cluster channel. messageType={}, clientId={}, remote={}, local={}",
                        messageType, channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                channel.close();
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
                logger.error("Username and password cannot be null. uid={}", clientId);
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

        listener.clusterChannelAuth(channel, message, this);
    }

    private void processPingReq(MqttChannel channel) throws Exception {
        logger.debug("Cluster channel ping req. clientId={}, remote={}, local={}",
                channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
        MqttMessage ackMessage = MqttAckMessageFactory.mqttPingReqAckMessage();
        channel.writeAndFlush(ackMessage);
    }

    private void processPublish(MqttChannel channel, MqttPublishMessage message) {
        MqttQoS qoS = message.fixedHeader().qosLevel();
        switch (qoS) {
            case AT_LEAST_ONCE: {
                String topicName = message.variableHeader().topicName();
                if (!topicName.equals(NetworkConstants.CLUSTER_PUSH_TOPIC)) {
                    logger.error("Cluster channel has not supported topic, close channel, topicName={}, clientId={}, remote={}, local={}, ",
                            topicName, channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                    channel.close();
                    return;
                }

                try {
                    pushMessage(channel, message);
                } catch (Exception e) {
                    logger.error("Cluster channel push message exception, clientId={}, remote={}, local={}",
                            channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
                }
                break;
            }

            default: {
                logger.error("QoS level ={} has not been supported, close cluster channel. clientId={}, remote={}, local={}",
                        qoS, channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                channel.close();
                break;
            }
        }
    }

    private void pushMessage(MqttChannel channel, MqttPublishMessage message) throws Exception {
        PushMessage pushMessage = listener.pushMessage(message);
        if (null != pushMessage) {
            pushRouter.pushMessage(pushMessage);
        }

        int messageId = message.variableHeader().packetId();
        MqttPubAckMessage ackMessage = MqttAckMessageFactory.mqttPubAckMessage(messageId);
        channel.writeAndFlush(ackMessage);
    }

    @Override
    public void processDisConnect(MqttChannel channel) {
        channelMap.remove(channel.getClientId());
    }

    @Override
    public void channelAuth(MqttChannel channel) throws Exception {
        if (!channel.getChannel().isActive()) {
            logger.error("Cluster channel has closed, clientId={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return;
        }

        //认证失败
        if (!channel.isAuth()) {
            logger.error("Cluster channel channelAuth fail, clientId={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            MqttConnAckMessage ackMessage = MqttAckMessageFactory.mqttConnAckMessage(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false);
            channel.writeAndFlush(ackMessage);
            channel.close();
            return;
        }

        //认证成功
        MqttConnAckMessage ackMessage = MqttAckMessageFactory.mqttConnAckMessage(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
        channel.writeAndFlush(ackMessage);

        //注册通道
        channelMap.put(channel.getClientId(), channel);

        //自动订阅topic

        //通道握手成功
        logger.debug("Mqtt cluster channel connect success, clientId={}, remote={}, local={}",
                channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
    }
}
