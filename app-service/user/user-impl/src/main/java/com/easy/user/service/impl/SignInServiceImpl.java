package com.easy.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.easy.common.enums.model.CommonEnums;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.push.rpcao.PushServerRpcAo;
import com.easy.push.rpcapi.GamePushRpcService;
import com.easy.push.rpcvo.PushServerRpcVo;
import com.easy.user.ao.PushServer;
import com.easy.user.ao.SignInAo;
import com.easy.user.model.mysql.dao.SignInLogMapper;
import com.easy.user.model.mysql.dao.SignInMapper;
import com.easy.user.model.mysql.po.SignIn;
import com.easy.user.model.mysql.po.SignInLog;
import com.easy.user.model.mysql.po.User;
import com.easy.user.model.mysql.po.builder.SignInBuilder;
import com.easy.user.model.mysql.po.builder.SignInLogBuilder;
import com.easy.user.service.SignInService;
import com.easy.user.token.TokenBuilder;
import com.easy.user.vo.SignInVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {
    private static final Logger logger = LoggerFactory.getLogger(SignInServiceImpl.class);

    @Autowired
    private SignInMapper signInMapper;
    @Autowired
    private SignInLogMapper signInLogMapper;
    @Autowired
    private GamePushRpcService gamePushRpcService;

    @Override
    public SignInVo signInWithTX(AppRequest req, SignInAo ao, User user, SignInLog signInLog) {
        PushServer pushServer = getPushServer(user);

        String token = TokenBuilder.create(user.getUid(), req.getBusinessType(), req.getAgentMode(), req.getDeviceId());

        SignIn signIn = SignInBuilder.build(user.getUid(), signInLog, token);
        int rows = signInMapper.updateByPrimaryKeySelective(signIn);
        if (0 == rows) {
            signInMapper.insertSelective(signIn);
        }

        SignInLog updateLog = SignInLogBuilder.update(signInLog, user.getUid(), token, CommonEnums.Result.successful);
        signInLogMapper.updateByPrimaryKeySelective(updateLog);

        SignInVo vo = new SignInVo();
        vo.setUid(user.getUid());
        vo.setToken(token);
        vo.setNickName(user.getNickName());
        vo.setHeadPictureUrl(user.getHeadPictureUrl());
        vo.setRealNameStatus(user.getRealNameStatus());
        vo.setPushServer(pushServer);
        return vo;
    }

    private PushServer getPushServer(User user) {
        PushServerRpcAo rpcAo = new PushServerRpcAo();
        rpcAo.setUid(user.getUid());

        PushServerRpcVo rpcVo = gamePushRpcService.queryPushServer(rpcAo);
        if (BaseRpcVo.isFail(rpcVo)) {
            logger.error("获取推送服务失败, rpcAo={}, rpcVo={}", JSON.toJSONString(rpcAo), JSON.toJSONString(rpcVo));
            throw BaseRpcVo.createBusinessException(rpcVo);
        }

        PushServer pushServer = new PushServer();
        pushServer.setHostname(rpcVo.getHostname());
        pushServer.setPort(rpcVo.getPort());
        return pushServer;
    }
}
