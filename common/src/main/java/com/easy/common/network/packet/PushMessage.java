package com.easy.common.network.packet;

import java.io.Serializable;

public class PushMessage implements Serializable {
    private static final long serialVersionUID = 4510826821366350281L;

    private long messageId;
    private String clientId;
    private String topic;
    private int mqttMessageId;
    private long uid;
    private String data;
    private int pushStatus;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMqttMessageId() {
        return mqttMessageId;
    }

    public void setMqttMessageId(int mqttMessageId) {
        this.mqttMessageId = mqttMessageId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(int pushStatus) {
        this.pushStatus = pushStatus;
    }
}
