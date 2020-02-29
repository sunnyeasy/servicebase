package com.easy.push.transport.netty4;

import com.easy.common.transport.packet.push.PushMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

public interface MqttClusterListener {
    void clusterChannelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception;

    //推送消息给终端用户（安卓,ios,h5,小程序）
    PushMessage pushMessage(MqttPublishMessage message) throws Exception;
}
