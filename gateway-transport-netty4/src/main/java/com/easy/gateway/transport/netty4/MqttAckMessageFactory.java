package com.easy.gateway.transport.netty4;

import io.netty.handler.codec.mqtt.*;

public class MqttAckMessageFactory {
    public static MqttConnAckMessage mqttConnAckMessage(MqttConnectReturnCode returnCode, boolean sessionPresent) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(returnCode, sessionPresent);
        return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
    }

}
