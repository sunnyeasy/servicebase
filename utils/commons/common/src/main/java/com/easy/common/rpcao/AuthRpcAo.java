package com.easy.common.rpcao;

import com.easy.common.enums.AgentMode;
import com.easy.common.enums.BusinessType;

import java.io.Serializable;

public class AuthRpcAo implements Serializable {
    private static final long serialVersionUID = 6000993409112309335L;

    private String token;

    private BusinessType businessType;//业务类型

    private AgentMode agentMode;//app类型:html,ios,anriod,微信小程序,微信公众号

    private String deviceId;

    private String imei;

    private String version;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public AgentMode getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(AgentMode agentMode) {
        this.agentMode = agentMode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
