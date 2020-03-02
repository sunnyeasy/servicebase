package com.easy.common.transport.packet.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PushMessage implements Serializable {
    private static final long serialVersionUID = 4510826821366350281L;

    private long messageId;
    private String clientId;
    private String topic;
    private int mqttMessageId;
    private long uid;
    private String data;
    private int pushStatus;
    private int pushCount;
    private long createTime;
    private long lastPushTime;
    private long successTime;
    private long timeoutTime;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("messageId", String.valueOf(messageId));
        map.put("clientId", null == clientId ? "" : clientId);
        map.put("topic", null == topic ? "" : topic);
        map.put("mqttMessageId", String.valueOf(mqttMessageId));
        map.put("uid", String.valueOf(uid));
        map.put("data", data);
        map.put("pushStatus", String.valueOf(pushStatus));
        map.put("pushCount", String.valueOf(pushCount));
        map.put("createTime", String.valueOf(createTime));
        map.put("lastPushTime", String.valueOf(lastPushTime));
        map.put("successTime", String.valueOf(successTime));
        map.put("timeoutTime", String.valueOf(timeoutTime));
        return map;
    }

    public Map<String, String> toUpdateMap() {
        Map<String, String> map = new HashMap<>();
        map.put("messageId", String.valueOf(messageId));
        map.put("clientId", null == clientId ? "" : clientId);
        map.put("topic", null == topic ? "" : topic);
        map.put("pushStatus", String.valueOf(pushStatus));
        map.put("lastPushTime", String.valueOf(lastPushTime));
        map.put("successTime", String.valueOf(successTime));
        map.put("timeoutTime", String.valueOf(timeoutTime));
        return map;
    }

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

    public int getPushCount() {
        return pushCount;
    }

    public void setPushCount(int pushCount) {
        this.pushCount = pushCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(long lastPushTime) {
        this.lastPushTime = lastPushTime;
    }

    public long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(long successTime) {
        this.successTime = successTime;
    }

    public long getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(long timeoutTime) {
        this.timeoutTime = timeoutTime;
    }
}
