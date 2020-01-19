package com.easy.push.transport.netty4;

import com.easy.common.exception.BusinessException;
import com.easy.common.network.packet.PushMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MqttChannel {
    private static final Logger logger = LoggerFactory.getLogger(MqttChannel.class);

    private String clientId;
    private Channel channel;
    private boolean auth;

    private volatile boolean isPushing = false;
    private Queue<PushMessage> pushQueue = new LinkedBlockingQueue<>();
    private AtomicInteger messageId = new AtomicInteger(1);

    public MqttChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean writeAndFlush(Object message) throws Exception {
        if (!channel.isActive()) {
            logger.error("Channel is inactive, clientId={}, remote={}, local={}", clientId, channel.remoteAddress(), channel.localAddress());
            return false;
        }

        channel.writeAndFlush(message);
        return true;
    }

    public void close() {
        channel.close();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isPushing() {
        return isPushing;
    }

    public void setPushing(boolean pushing) {
        isPushing = pushing;
    }

    public void addPushMessage(PushMessage message) {
        pushQueue.add(message);
    }

    public synchronized PushMessage peekPushMessage() {
        if (isPushing) {
            return null;
        }

        PushMessage message = pushQueue.peek();
        if (null != message) {
            isPushing = true;
        }
        return message;
    }

    public synchronized PushMessage pollPushMessage(int mqttMessageId) {
        if (!isPushing) {
            logger.error("Channel pushing status is not true, isPushing={}, clientId={}, remote={}, local={}",
                    isPushing, clientId, channel.remoteAddress(), channel.localAddress());
            throw new BusinessException(MqttResponseCode.CHANNEL_PUSHING_STATUS_ERROR);
        }

        PushMessage message = pushQueue.peek();
        if (null == message) {
            logger.error("Channel pushing status is not true, isPushing={}, pushQueue.size={}, clientId={}, remote={}, local={}",
                    isPushing, pushQueue.size(), clientId, channel.remoteAddress(), channel.localAddress());
            throw new BusinessException(MqttResponseCode.CHANNEL_PUSHING_STATUS_ERROR);
        }

        if (mqttMessageId != message.getMqttMessageId()) {
            logger.error("Waitting channel puback messageId is wrong, ackMqttMessageId={}, mqttMessageId={}, clientId={}, remote={}, local={}",
                    mqttMessageId, message.getMqttMessageId(), clientId, channel.remoteAddress(), channel.localAddress());
            throw new BusinessException(MqttResponseCode.CHANNEL_PUBACK_MESSAGE_ID_ERROR);
        }

        message = pushQueue.poll();
        isPushing = false;
        return message;
    }

    public int nextMessageId() {
        return messageId.getAndIncrement();
    }
}
