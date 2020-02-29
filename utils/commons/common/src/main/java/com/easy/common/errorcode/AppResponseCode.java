package com.easy.common.errorcode;

import java.io.Serializable;

public class AppResponseCode implements Serializable {
    private static final long serialVersionUID = 3877879217647498915L;

    private int errorCode;
    private String message;

    public AppResponseCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public AppResponseCode(ResponseCode code) {
        this.errorCode = code.getErrorCode();
        this.message = code.failReason();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "errorcode=" + errorCode
                + ", message=" + message;
    }
}
