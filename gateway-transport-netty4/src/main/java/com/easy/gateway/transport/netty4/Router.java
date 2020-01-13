package com.easy.gateway.transport.netty4;

public interface Router {
    void auth(MqttChannel channel);
}
