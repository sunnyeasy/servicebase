package com.easy.common.transport.packet.gateway;

import com.easy.common.code.ResponseCode;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = -359925595814289255L;

    private ResponseCode code = ResponseCode.SUCESSFUL;
    private String data;
    private String params;

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public static boolean isFail(RpcResponse response) {
        if (null == response
                || response.code.getCode() != ResponseCode.SUCESSFUL.getCode()){
            return true;
        }
        return false;
    }

    public static boolean isSuccessful(RpcResponse response) {
        if (null != response
                && response.code.getCode() == ResponseCode.SUCESSFUL.getCode()){
            return true;
        }
        return false;
    }
}
