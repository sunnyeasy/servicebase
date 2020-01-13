package com.easy.common.exception;

import com.easy.common.code.ResponseCode;

public class BusinessException extends RuntimeException {
    private ResponseCode code;

    public BusinessException(ResponseCode code) {
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public static ResponseCode parseResponseCode(Throwable e){
        Throwable parentCause = e;
        Throwable cause = parentCause.getCause();
        while( null != cause ){
            parentCause = cause;
            cause = cause.getCause();
        }

        if(parentCause instanceof BusinessException){
            return ((BusinessException) parentCause).getCode();
        }else {
            return ResponseCode.APPLICATION_EXCEPTION;
        }
    }
}
