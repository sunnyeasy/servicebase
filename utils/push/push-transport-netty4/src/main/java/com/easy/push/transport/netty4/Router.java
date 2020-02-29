package com.easy.push.transport.netty4;

import io.netty.handler.codec.mqtt.MqttMessage;

public interface Router {
    //身份认证
    void channelAuth(MqttChannel channel, Exception authException) throws Exception;

    void processMessage(MqttChannel channel, MqttMessage message) throws Exception;

    void processDisConnect(MqttChannel channel);

}
