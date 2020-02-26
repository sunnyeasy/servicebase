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

    public static boolean isSuccessful(BaseRpcVo rpcVo) {
        if (null != rpcVo && rpcVo.code.getCode() == ResponseCode.SUCESSFUL.getCode()) {
            return true;
        }
        return false;
    }

    public static boolean isFail(BaseRpcVo rpcVo) {
        if (null == rpcVo || rpcVo.code.getCode() != ResponseCode.SUCESSFUL.getCode()) {
            return true;
        }
        return false;
    }

}
