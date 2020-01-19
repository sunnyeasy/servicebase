package com.easy.push.transport.netty4;

import com.easy.common.code.ResponseCode;

public class MqttResponseCode {
    public static final ResponseCode MESSAGE_TYPE_NOT_SUPPORT = new ResponseCode(40000, "消息类型错误");

    public static final ResponseCode CHANNEL_PUSHING_STATUS_ERROR = new ResponseCode(40501, "通道Pushing状态错误");
    public static final ResponseCode CHANNEL_PUBACK_MESSAGE_ID_ERROR = new ResponseCode(40502, "通道收到PubAck消息id错误");

}
