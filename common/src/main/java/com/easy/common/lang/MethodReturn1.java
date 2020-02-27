package com.easy.common.lang;

public class MethodReturn1<T> {
    private MethodResult result;
    private T data;

    public MethodResult getResult() {
        return result;
    }

    public void setResult(MethodResult result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
