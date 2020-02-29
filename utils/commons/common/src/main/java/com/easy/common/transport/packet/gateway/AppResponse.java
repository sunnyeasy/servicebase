package com.easy.common.transport.packet.gateway;

import com.easy.common.errorcode.AppResponseCode;
import com.easy.common.errorcode.ResponseCode;

import java.io.Serializable;

@Deprecated
public class AppResponse implements Serializable {
    private static final long serialVersionUID = -2255949186683676389L;

    private AppResponseCode code;

    private String data;

    private String params;

    public AppResponseCode getCode() {
        return code;
    }

    public void setCode(AppResponseCode code) {
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
