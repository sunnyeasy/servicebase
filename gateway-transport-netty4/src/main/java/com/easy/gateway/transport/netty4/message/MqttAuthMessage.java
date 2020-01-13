package com.easy.gateway.transport.netty4.message;

import com.easy.gateway.transport.netty4.MqttChannel;

public class MqttAuthMessage {
    private MqttChannel channel;
    private String username;
    private String password;

    public MqttAuthMessage(MqttChannel channel, String username, String password) {
        this.channel = channel;
        this.username = username;
        this.password = password;
    }

    public MqttChannel getChannel() {
        return channel;
    }

    public void setChannel(MqttChannel channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
