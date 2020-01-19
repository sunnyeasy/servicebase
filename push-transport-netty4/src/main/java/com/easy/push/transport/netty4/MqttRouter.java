package com.easy.push.transport.netty4;

import com.easy.common.exception.BusinessException;
import com.easy.common.network.packet.PushMessage;
import com.easy.common.thread.StandardThreadExecutor;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

/**
 * 注意:
 * 面向终端用户(安卓，ios，h5，小程序),未实现完整的MQTT协议
 * 1.只用到MQTT协议的部分命令
 * CONNECT
 * DISCONNECT
 * PUBLISH
 * PUBACK
 *
 * 2.QoS只支持0级别(at most once)
 */
public class MqttRouter implements Router, PushRouter {
    private static final Logger logger = LoggerFactory.getLogger(MqttRouter.class);

    private MqttConfig mqttConfig;

    private MqttListener mqttListener;

    private Map<String, MqttChannel> channelMap;

    private StandardThreadExecutor pushStandardThreadExecutor;

    public MqttRouter(MqttConfig mqttConfig, MqttListener mqttListener) {
        this.mqttConfig = mqttConfig;
        this.mqttListener = mqttListener;
        this.channelMap = new ConcurrentHashMap<>();
        this.pushStandardThreadExecutor =  new StandardThreadExecutor(mqttConfig.getMinPusherThread(), mqttConfig.getMaxPusherThread(), mqttConfig.getPusherQueueSize(), new DefaultThreadFactory("mqttTcpPusher-" + mqttConfig.getPort(), true));
    }

    @Override
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
                logger.info("Client close channel, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                processDisConnect(channel);
                break;

            case PINGREQ:
                processPingReq(channel);
                break;

            case PUBACK:
                processPubAck(channel, (MqttPubAckMessage) message);
                break;

            default:
                logger.info("Message type has not been supported, close channel. messageType={}, clientId={}, remote={}, local={}", messageType, channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
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
        mqttListener.channelAuth(channel, message, this);
    }

    private void processPingReq(MqttChannel channel) throws Exception {
        logger.debug("Channel ping req. clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
        MqttMessage ackMessage = MqttAckMessageFactory.mqttPingReqAckMessage();
        channel.writeAndFlush(ackMessage);
    }

    @Override
    public void processDisConnect(MqttChannel channel) {
        channelMap.remove(channel.getClientId());
    }

    @Override
    public void channelAuth(MqttChannel channel) throws Exception {
        if (!channel.getChannel().isActive()) {
            logger.error("Channel has closed, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return;
        }

        //认证失败
        if (!channel.isAuth()) {
            logger.error("Channel channelAuth fail, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
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
        logger.debug("Mqtt channel connect success, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
    }

    @Override
    public void pushMessage(PushMessage message) {
        MqttChannel channel = channelMap.get(message.getClientId());
        if (null == channel) {
            logger.error("Push message error, Channel is valid. clientId={}, uid={}", message.getClientId(), message.getUid());
            return;
        }

        channel.addPushMessage(message);

        if (!channel.isPushing()) {
            executePushMessage(channel);
        }
    }

    private void executePushMessage(MqttChannel channel) {
        try {
            pushStandardThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        pushMessage(channel);
                    } catch (Exception e) {
                        logger.error("Push message to terminal exception, close channel, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
                        channel.close();
                    }
                }
            });
        } catch (RejectedExecutionException rejectException) {
            logger.error("Push process thread pool is full, reject, active={}, poolSize={}, corePoolSize={}, maxPoolSize={}, taskCount={}",
                    pushStandardThreadExecutor.getActiveCount(), pushStandardThreadExecutor.getPoolSize(), pushStandardThreadExecutor.getCorePoolSize(),
                    pushStandardThreadExecutor.getMaximumPoolSize(), pushStandardThreadExecutor.getTaskCount());
        }
    }

    private void pushMessage(MqttChannel channel) throws Exception {
        PushMessage message = channel.peekPushMessage();
        if (null == message) {
            return;
        }

        message.setMessageId(channel.nextMessageId());
        if (channel.getChannel().isActive()) {
            MqttPublishMessage ackMessage = MqttAckMessageFactory.mqttPublishMessage(message);
            channel.writeAndFlush(ackMessage);
        }
    }

    private void processPubAck(MqttChannel channel, MqttPubAckMessage message) {
        int mqttMessageId = message.variableHeader().messageId();
        PushMessage pushMessage = null;
        try {
            pushMessage = channel.pollPushMessage(mqttMessageId);
        } catch (BusinessException e) {
            if (e.getCode().getCode() == MqttResponseCode.CHANNEL_PUBACK_MESSAGE_ID_ERROR.getCode()) {
                return;
            }
            throw e;
        }

        if (null == pushMessage) {
            logger.warn("Channel pushing status error, Why ?. clientId={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return;
        }

        try {
            mqttListener.finishPushMessage(pushMessage);
        } catch (Exception e) {
            logger.error("Channel finish push message exception. clientId={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
        }

        executePushMessage(channel);
    }
}
