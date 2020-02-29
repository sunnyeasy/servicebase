package com.easy.user.model.mysql.po;

public class SignInKey {
    private Long uid;

    private String businessType;

    private String agentMode;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(String agentMode) {
        this.agentMode = agentMode == null ? null : agentMode.trim();
    }
}