package com.easy.user.ao;

import com.easy.constant.enums.AgentMode;
import com.easy.constant.enums.BusinessType;

public class Token {
    private Long uid;
    private AgentMode agentMode;//app类型:html,ios,anriod,微信小程序,微信公众号
    private BusinessType businessType;//业务类型
    private String deviceId;
    private Long expiredTime;
    private Long magic;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public AgentMode getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(AgentMode agentMode) {
        this.agentMode = agentMode;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Long getMagic() {
        return magic;
    }

    public void setMagic(Long magic) {
        this.magic = magic;
    }
}
