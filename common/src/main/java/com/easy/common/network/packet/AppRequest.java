package com.easy.common.network.packet;

import java.io.Serializable;

public class AppRequest implements Serializable {
    private static final long serialVersionUID = -2255949186683676389L;

    private String url;

    private String data;

    private String version;

    private String agentMode;//app类型:html,ios,anriod,微信小程序,微信公众号

    private String businessType;//业务类型:欢乐斗地主

    private String deviceId;

    private String imei;

    private String params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAgentMode() {
        return agentMode;
    }

    public void setAgentMode(String agentMode) {
        this.agentMode = agentMode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
