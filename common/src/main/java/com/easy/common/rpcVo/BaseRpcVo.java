package com.easy.common.rpcVo;

import com.easy.common.code.ResponseCode;

import java.io.Serializable;

public class BaseRpcVo implements Serializable {
    private static final long serialVersionUID = 1068035106945158408L;

    private ResponseCode code;

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }
}
