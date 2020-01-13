package com.easy.gateway.transport.netty4;

import com.easy.gateway.transport.netty4.message.MqttAuthMessage;

public interface MqttListener {
    void auth(MqttAuthMessage message, Router router);

    String subscribeTopic(String clientId);
}
