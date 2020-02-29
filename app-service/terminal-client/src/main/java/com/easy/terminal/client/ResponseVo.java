package com.easy.terminal.client;

import com.easy.common.errorcode.AppResponseCode;

public class ResponseVo<T> {
    private AppResponseCode code;
    private T data;

    public AppResponseCode getCode() {
        return code;
    }

    public void setCode(AppResponseCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
