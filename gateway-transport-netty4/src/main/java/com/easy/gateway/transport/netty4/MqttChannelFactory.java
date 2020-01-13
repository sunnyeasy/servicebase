package com.easy.gateway.transport.netty4;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class MqttChannelFactory {
    private static final AttributeKey<Object> ATTR_KEY_MQTT_CHANNEL = AttributeKey.valueOf("mqttChannel");

    public static MqttChannel channel(Channel channel) {
        MqttChannel mqttChannel = (MqttChannel) channel.attr(ATTR_KEY_MQTT_CHANNEL).get();
        if (null == mqttChannel) {
            mqttChannel = new MqttChannel(channel);
            channel.attr(ATTR_KEY_MQTT_CHANNEL).set(mqttChannel);
        }
        return mqttChannel;
    }
}
