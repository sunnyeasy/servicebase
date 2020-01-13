package com.easy.common.rpcVo;

public class BaseRecordRpcVo<T> extends BaseRpcVo {
    private static final long serialVersionUID = 296893306190111558L;

    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
