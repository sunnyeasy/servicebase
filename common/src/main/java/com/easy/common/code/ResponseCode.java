package com.easy.common.code;

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
    public static final ResponseCode DATA_RECORD_NOT_FOUND = new ResponseCode(5003, "数据记录不存在");
    public static final ResponseCode HANDLER_DEFINED_ERROR = new ResponseCode(5004, "Handler定义错误");

    //网关通用错误码
    public static final ResponseCode UNAUTHORIZED = new ResponseCode(40401, "身份未通过认证");
    public static final ResponseCode NOT_FOUND = new ResponseCode(40404, "Not found");
    public static final ResponseCode METHOD_NOT_ALLOWED = new ResponseCode(40405, "方法不支持");
    public static final ResponseCode INTERNAL_SERVER_ERROR = new ResponseCode(40500, "系统繁忙");


    private static final long serialVersionUID = 531096289595529724L;

    private int code;
    private String message;
    private String reason;

    public ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseCode(ResponseCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
        return "code=" + code
                + ", message=" + message
                + ", reason=" + reason;
    }

}
