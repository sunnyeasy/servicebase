package com.easy.common.rpcVo;

import java.util.List;

public class BaseListRpcVo<T> extends BaseRpcVo {
    private static final long serialVersionUID = -1614963300877685597L;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
