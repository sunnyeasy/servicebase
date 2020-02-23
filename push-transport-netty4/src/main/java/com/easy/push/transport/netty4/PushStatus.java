package com.easy.push.transport.netty4;

public enum PushStatus {
    waiting(0),
    pushing(5),
    success(10),
    timeout(15)
    ;

    private PushStatus(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
