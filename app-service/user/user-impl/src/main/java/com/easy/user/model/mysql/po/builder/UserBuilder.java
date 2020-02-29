package com.easy.user.model.mysql.po.builder;

import com.easy.common.id.IdUtils;
import com.easy.constant.enums.user.model.UserEnums;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.model.mysql.po.User;

import java.util.Date;

public class UserBuilder {
    public static User build(SignUpAo ao, SignUpLog log) {
        User user = new User();
        user.setUid(IdUtils.getInstance().createUid());
        user.setMobile(ao.getMobile());
        user.setPassword(ao.getPassword());
        user.setUserStatus(UserEnums.UserStatus.normal.getCode());
        user.setNickName(null);
        user.setHeadPictureUrl(null);
        user.setRealNameStatus(UserEnums.RealNameStatus.no.getCode());
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }
}
