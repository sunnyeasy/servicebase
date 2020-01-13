package com.easy.common.network.packet;

import com.easy.common.code.ResponseCode;

import java.io.Serializable;

public class AppResponse implements Serializable {
    private static final long serialVersionUID = -2255949186683676389L;

    private ResponseCode code;

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
}
