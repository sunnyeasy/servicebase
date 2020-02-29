package com.easy.user.model.mysql.po.builder;

import com.easy.common.enums.model.CommonEnums;
import com.easy.common.enums.model.ResultUtil;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.po.SignUpLog;

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

    public static SignUpLog update(SignUpLog log, Long uid, CommonEnums.Result result){
        log.setUid(uid);
        log.setResult(result.getCode());
        log.setUpdateTime(new Date());

        SignUpLog updateLog = new SignUpLog();
        updateLog.setId(log.getId());
        updateLog.setUid(log.getUid());
        updateLog.setResult(log.getResult());
        updateLog.setUpdateTime(log.getUpdateTime());
        return updateLog;
    }

    public static SignUpLog update(SignUpLog log, ResponseCode code) {
        ResultUtil.buildResult(log, code);
        log.setUpdateTime(new Date());

        SignUpLog updateLog = new SignUpLog();
        updateLog.setId(log.getId());
        updateLog.setResult(log.getResult());
        updateLog.setFailCode(log.getFailCode());
        updateLog.setFailReason(log.getFailReason());
        updateLog.setUpdateTime(new Date());
        return updateLog;
    }
}
