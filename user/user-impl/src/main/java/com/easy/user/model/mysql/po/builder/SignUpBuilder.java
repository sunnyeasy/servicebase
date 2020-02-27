package com.easy.user.model.mysql.po.builder;

import com.easy.user.model.mysql.po.SignUp;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.model.mysql.po.User;

public class SignUpBuilder {
    public static SignUp build(User user, SignUpLog log){
        SignUp su=new SignUp();
        su.setUid(user.getUid());
        su.setBusinessType(log.getBusinessType());
        su.setAgentMode(log.getAgentMode());
        su.setIp(log.getIp());
        su.setLon(log.getLon());
        su.setLat(log.getLat());
        su.setCityCode(log.getCityCode());
        su.setDeviceId(log.getDeviceId());
        su.setReferrerUid(log.getReferrerUid());
        su.setSignUpId(log.getId());
        su.setSignUpTime(log.getSignUpTime());
        return su;
    }
}
