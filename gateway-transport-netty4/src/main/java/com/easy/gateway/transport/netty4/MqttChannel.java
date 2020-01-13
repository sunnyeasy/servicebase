package com.easy.gateway.transport.netty4;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttChannel {
    private static final Logger logger = LoggerFactory.getLogger(MqttChannel.class);

    private String clientId;
    private Channel channel;
    private boolean auth;

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
}
