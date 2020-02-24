package com.easy.push.transport.netty4;

import com.easy.common.network.packet.push.PushMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;

import java.util.List;

public interface MqttListener {
    //身份认证
    void channelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception;

    List<PushMessage> recoverPushMessage(MqttChannel channel);

    void prePushMessage(PushMessage message);

    void successPushMessage(PushMessage message);
}
