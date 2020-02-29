package com.easy.user.token;

import com.alibaba.fastjson.JSON;
import com.easy.common.enums.AgentMode;
import com.easy.common.enums.BusinessType;

import java.util.Base64;

public class TokenBuilder {
    public static String create(long uid, BusinessType businessType, AgentMode agentMode, String deviceId) {
        Token token = new Token();
        token.setUid(uid);
        token.setBusinessType(businessType);
        token.setAgentMode(agentMode);
        token.setDeviceId(deviceId);
        token.setExpiredTime(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000);
        token.setMagic(System.currentTimeMillis());

        String json = JSON.toJSONString(token);
        String t = Base64.getEncoder().encodeToString(json.getBytes());
        return t;
    }
}
