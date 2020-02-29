package com.easy.common.errorcode;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ResponseCode implements Serializable {
    public static final ResponseCode SUCESSFUL = new ResponseCode(1, "响应成功");

    public static final ResponseCode FAIL = new ResponseCode(4000, "失败");
    public static final ResponseCode UNKNOWN_ERROR = new ResponseCode(4001, "未知错误");
    public static final ResponseCode PARAMETERS_ERROR = new ResponseCode(4002, "请求参数错误");
    public static final ResponseCode URL_ERROR = new ResponseCode(4003, "请求url错误");

    public static final ResponseCode APPLICATION_EXCEPTION = new ResponseCode(5000, "系统繁忙");
    public static final ResponseCode APPLICATION_NOT_SUPPORTED = new ResponseCode(5001, "系统暂不支持");
    public static final ResponseCode CONFIG_ERROR = new ResponseCode(5002, "服务配置错误");
    public static final ResponseCode NULL_DATA = new ResponseCode(5003, "数据记录不存在");
    public static final ResponseCode HANDLER_DEFINED_ERROR = new ResponseCode(5004, "Handler定义错误");


    //推送服务错误码
    public static final ResponseCode MESSAGE_TYPE_NOT_SUPPORT = new ResponseCode(40000, "消息类型错误");
    public static final ResponseCode UNAUTHORIZED_PARAMETERS_ERROR = new ResponseCode(40401, "认证请求参数错误");
    public static final ResponseCode CHANNEL_PUSHING_STATUS_ERROR = new ResponseCode(40501, "通道Pushing状态错误");
    public static final ResponseCode CHANNEL_PUBACK_MESSAGE_ID_ERROR = new ResponseCode(40502, "通道收到PubAck消息id错误");

    //网关错误码
    public static final ResponseCode UNAUTHORIZED = new ResponseCode(400401, "身份未通过认证");
    public static final ResponseCode NOT_FOUND = new ResponseCode(400404, "Not found");
    public static final ResponseCode METHOD_NOT_ALLOWED = new ResponseCode(400405, "方法不支持");
    public static final ResponseCode INTERNAL_SERVER_ERROR = new ResponseCode(400500, "系统繁忙");


    private static final long serialVersionUID = 531096289595529724L;

    private int errorCode;
    private String message;
    private String reason;

    public ResponseCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseCode(ResponseCode code) {
        this.errorCode = code.getErrorCode();
        this.message = code.getMessage();
        this.reason = code.getReason();
    }

    public ResponseCode build(String reason) {
        ResponseCode code = new ResponseCode(this);
        code.setReason(reason);
        return code;
    }

    public String failReason() {
        if (StringUtils.isEmpty(reason)) {
            return message;
        }

        return message + "|" + reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String toString() {
        if (StringUtils.isEmpty(reason)) {
            return "errorcode=" + errorCode
                    + ", message=" + message;
        }

        return "errorcode=" + errorCode
                + ", message=" + message
                + "|" + reason;
    }

}
