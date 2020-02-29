package com.easy.push.rpcao;

import java.io.Serializable;

public class PushServerRpcAo implements Serializable {
    private static final long serialVersionUID = -344550526889615852L;

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
