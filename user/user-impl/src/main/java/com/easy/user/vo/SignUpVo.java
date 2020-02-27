package com.easy.user.vo;

import java.io.Serializable;

public class SignUpVo implements Serializable {
    private static final long serialVersionUID = 3666827296472404911L;

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
