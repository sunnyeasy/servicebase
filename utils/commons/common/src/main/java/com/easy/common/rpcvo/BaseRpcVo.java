package com.easy.common.rpcvo;

import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;

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
        if (null != rpcVo && rpcVo.code.getErrorCode() == ResponseCode.SUCESSFUL.getErrorCode()) {
            return true;
        }
        return false;
    }

    public static boolean isFail(BaseRpcVo rpcVo) {
        if (null == rpcVo || rpcVo.code.getErrorCode() != ResponseCode.SUCESSFUL.getErrorCode()) {
            return true;
        }
        return false;
    }

    public static BusinessException createBusinessException(Object rpcVo) {
        if (null == rpcVo) {
            return new BusinessException(ResponseCode.UNKNOWN_ERROR);
        }

        BaseRpcVo baseRpcVo = (BaseRpcVo) rpcVo;
        return new BusinessException(baseRpcVo.getCode());
    }
}
