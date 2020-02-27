package com.easy.common.exception;

import com.easy.common.errorcode.ResponseCode;

public class BusinessException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private String reason;

    public BusinessException(ResponseCode code) {
        super(code.toString());
        this.errorCode = code.getErrorCode();
        this.errorMessage = code.getMessage();
        this.reason = code.getReason();
    }

    public BusinessException(ResponseCode code, String reason) {
        super(code.toString() + ", reason=" + reason);
        this.errorCode = code.getErrorCode();
        this.errorMessage = code.getMessage();
        this.setReason(reason);
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode
                + ", errorMessage=" + errorMessage
                + ", reason=" + reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ResponseCode parseResponseCode(Throwable e) {
        Throwable parentCause = e;
        Throwable cause = parentCause.getCause();
        while (null != cause) {
            parentCause = cause;
            cause = cause.getCause();
        }

        if (parentCause instanceof BusinessException) {
            BusinessException be = (BusinessException) parentCause;
            ResponseCode code = new ResponseCode(be.getErrorCode(), be.getErrorMessage());
            code.setReason(be.getReason());
            return code;
        } else {
            return ResponseCode.APPLICATION_EXCEPTION;
        }
    }

}
