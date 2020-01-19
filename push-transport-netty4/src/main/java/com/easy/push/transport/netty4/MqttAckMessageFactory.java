package com.easy.push.transport.netty4;

import com.easy.common.network.packet.PushMessage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;

public class MqttAckMessageFactory {
    public static MqttConnAckMessage mqttConnAckMessage(MqttConnectReturnCode returnCode, boolean sessionPresent) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(returnCode, sessionPresent);
        return new MqttConnAckMessage(fixedHeader, variableHeader);
    }

    public static MqttMessage mqttPingReqAckMessage() {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage message = new MqttMessage(fixedHeader);
        return message;
    }

    public static MqttPublishMessage mqttPublishMessage(PushMessage message) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(message.getTopic(), message.getMqttMessageId());
        MqttPublishMessage ackMessage = new MqttPublishMessage(fixedHeader, variableHeader, Unpooled.copiedBuffer(message.getData().getBytes()));
        return ackMessage;
    }

    public static MqttPubAckMessage mqttPubAckMessage(int messageId) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttPubAckMessage ackMessage = new MqttPubAckMessage(fixedHeader, variableHeader);
        return ackMessage;
    }
}
