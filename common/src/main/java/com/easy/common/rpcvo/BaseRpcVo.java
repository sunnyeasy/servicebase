package com.easy.common.rpcvo;

import com.easy.common.code.ResponseCode;

import java.io.Serializable;

public class BaseRpcVo implements Serializable {
    private static final long serialVersionUID = 1068035106945158408L;

    private ResponseCode code = ResponseCode.SUCESSFUL;

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public boolean isSuccessful() {
        return this.code.getCode() == ResponseCode.SUCESSFUL.getCode();
    }
}
