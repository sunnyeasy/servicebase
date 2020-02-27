package com.easy.common.lang;

public class MethodReturn2<T,U> {
    private MethodResult result = MethodResult.successful;
    private T data;
    private U data2;

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

    public U getData2() {
        return data2;
    }

    public void setData2(U data2) {
        this.data2 = data2;
    }
}
