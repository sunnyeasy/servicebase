package com.easy.common.rpcvo;

import com.easy.common.network.packet.gateway.RpcResponse;

public class AuthRpcVo extends BaseRpcVo {
    private static final long serialVersionUID = -8882450731624041677L;

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
