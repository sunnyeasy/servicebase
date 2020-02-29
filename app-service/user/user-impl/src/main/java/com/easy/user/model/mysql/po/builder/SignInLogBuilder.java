package com.easy.user.model.mysql.po.builder;

import com.easy.common.enums.model.CommonEnums;
import com.easy.common.enums.model.ResultUtil;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.ao.SignInAo;
import com.easy.user.model.mysql.po.SignInLog;

import java.util.Date;

public class SignInLogBuilder {
    public static SignInLog build(AppRequest req, SignInAo ao) {
        SignInLog log = new SignInLog();
        log.setId(null);
        log.setMobile(ao.getMobile());
        log.setUid(null);
        log.setSignInType(ao.getSignInType().name());
        log.setBusinessType(req.getBusinessType().name());
        log.setAgentMode(req.getAgentMode().name());
        log.setIp(req.getIp());
        log.setLon(ao.getLon());
        log.setLat(ao.getLat());
        log.setCityCode(ao.getCityCode());
        log.setDeviceId(req.getDeviceId());
        log.setToken(null);
        log.setVersion(req.getVersion());
        log.setResult(CommonEnums.Result.wait.getCode());
        log.setFailCode(null);
        log.setFailReason(null);
        log.setSignInTime(new Date());
        log.setUpdateTime(log.getSignInTime());
        return log;
    }

    public static SignInLog update(SignInLog log, Long uid, String token, CommonEnums.Result result) {
        log.setUid(uid);
        log.setToken(token);
        log.setResult(CommonEnums.Result.successful.getCode());
        log.setUpdateTime(new Date());

        SignInLog update=new SignInLog();
        update.setId(log.getId());
        update.setUid(log.getUid());
        update.setToken(log.getToken());
        update.setResult(log.getResult());
        update.setUpdateTime(new Date());
        return update;
    }

    public static SignInLog update(SignInLog log, Long uid, ResponseCode code){
        log.setUid(uid);
        ResultUtil.buildResult(log,code);
        log.setUpdateTime(new Date());

        SignInLog update=new SignInLog();
        update.setId(log.getId());
        update.setResult(log.getResult());
        update.setFailCode(log.getFailCode());
        update.setFailReason(log.getFailReason());
        update.setUpdateTime(log.getUpdateTime());
        return update;
    }
}
