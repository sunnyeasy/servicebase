package com.easy.push.transport.netty4;

import com.easy.common.network.packet.push.PushMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;

public interface MqttListener {
    //身份认证
    void channelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception;

    void finishPushMessage(PushMessage message);
}
