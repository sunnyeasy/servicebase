package com.easy.user.model.mysql.po.builder;

import com.easy.user.model.mysql.po.SignIn;
import com.easy.user.model.mysql.po.SignInLog;

import java.util.Date;

public class SignInBuilder {
    public static SignIn build(Long uid, SignInLog log, String token) {
        SignIn si = new SignIn();
        si.setUid(uid);
        si.setBusinessType(log.getBusinessType());
        si.setAgentMode(log.getAgentMode());
        si.setDeviceId(log.getDeviceId());
        si.setToken(token);
        si.setSignInId(log.getId());
        si.setAuthTime(new Date());
        return si;
    }
}
