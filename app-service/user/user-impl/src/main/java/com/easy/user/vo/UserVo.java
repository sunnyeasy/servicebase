package com.easy.user.vo;

import java.io.Serializable;

public class UserVo implements Serializable {
    private static final long serialVersionUID = -6387976278801891706L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
