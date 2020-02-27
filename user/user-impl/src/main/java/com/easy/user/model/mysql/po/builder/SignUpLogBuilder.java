package com.easy.user.model.mysql.po.builder;

import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.constant.model.enums.CommonEnums;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.po.SignUpLog;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class SignUpLogBuilder {
    public static SignUpLog build(SignUpAo ao, AppRequest req) {
        SignUpLog log = new SignUpLog();
        log.setId(null);
        log.setMobile(ao.getMobile());
        log.setUid(null);
        log.setBusinessType(req.getBusinessType().name());
        log.setAgentMode(req.getAgentMode().name());
        log.setIp(req.getIp());
        log.setLon(ao.getLon());
        log.setLat(ao.getLat());
        log.setCityCode(null);
        log.setDeviceId(ao.getDeviceId());
        log.setReferrerMobile(ao.getReferredMobile());
        log.setReferrerUid(null);
        log.setSignUpTime(new Date());
        log.setVersion(ao.getVersion());
        log.setResult(null);
        log.setFailCode(null);
        log.setFailReason(null);
        log.setUpdateTime(log.getSignUpTime());
        return log;
    }

    public static void buildResult(SignUpLog log, ResponseCode code) {
        log.setResult(CommonEnums.Result.fail.getCode());
        log.setFailCode(String.valueOf(code.getErrorCode()));
        if (StringUtils.isEmpty(code.getReason())) {
            log.setFailReason(code.getMessage());
        } else {
            log.setFailReason(code.getReason());
        }
    }
}
