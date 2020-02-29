package com.easy.user.handler;

import com.alibaba.fastjson.JSON;
import com.easy.common.enums.model.ResultUtil;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.handler.HandlerMapping;
import com.easy.common.rpcvo.BaseRecordRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.constant.enums.user.model.SignInLogEnums;
import com.easy.constant.errorcode.UserResponseCode;
import com.easy.user.ao.SignInAo;
import com.easy.user.model.mysql.dao.SignInLogMapper;
import com.easy.user.model.mysql.dao.SignInMapper;
import com.easy.user.model.mysql.dao.UserMapper;
import com.easy.user.model.mysql.po.SignInLog;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.model.mysql.po.User;
import com.easy.user.model.mysql.po.builder.SignInLogBuilder;
import com.easy.user.service.SignInService;
import com.easy.user.vo.SignInVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@HandlerMapping("/user/signIn")
public class SignInHandler {
    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignInLogMapper signInLogMapper;

    @Autowired
    private SignInService signInService;

    @HandlerMapping("/do")
    public BaseRecordRpcVo<SignInVo> signIn(AppRequest req, SignInAo ao) {
        if (StringUtils.isEmpty(ao.getMobile())) {
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "手机号错误");
        }

        if (StringUtils.isEmpty(ao.getPassword())) {
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "密码错误");
        }

        if (null == ao.getSignInType()) {
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "登陆方式错误");
        }

        switch (ao.getSignInType()) {
            case password:
                return signInByPassword(req, ao);

            case sms:
                return signInBySms(req, ao);

            default:
                throw new BusinessException(ResponseCode.APPLICATION_NOT_SUPPORTED, "系统暂不支持该登陆方式");
        }
    }

    private BaseRecordRpcVo<SignInVo> signInByPassword(AppRequest req, SignInAo ao) {
        User user = userMapper.selectByMobile(ao.getMobile());

        SignInLog signInLog = SignInLogBuilder.build(req, ao);
        if (null == user) {
            insertSignInLog(signInLog, ResponseCode.PARAMETERS_ERROR.build("手机号未注册"));
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "手机号未注册");
        }

        if (!user.getPassword().equals(ao.getPassword())) {
            insertSignInLog(signInLog, ResponseCode.PARAMETERS_ERROR.build("密码错误"));
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "密码错误");
        }

        signInLogMapper.insertSelective(signInLog);

        BaseRecordRpcVo<SignInVo> rpcVo = new BaseRecordRpcVo<>();
        SignInVo vo = null;
        try {
            vo = signInService.signInWithTX(req, ao, user, signInLog);
        } catch (Exception e) {
            logger.error("用户登陆失败, req={}", JSON.toJSONString(req), e);

            ResponseCode code = BusinessException.parseResponseCode(e);
            rpcVo.setCode(code);

            SignInLog updateLog = SignInLogBuilder.update(signInLog, user.getUid(), code);
            signInLogMapper.updateByPrimaryKeySelective(updateLog);
        }
        rpcVo.setData(vo);
        return rpcVo;
    }

    private BaseRecordRpcVo<SignInVo> signInBySms(AppRequest req, SignInAo ao) {
        SignInLog signInLog = SignInLogBuilder.build(req, ao);
        insertSignInLog(signInLog, ResponseCode.APPLICATION_NOT_SUPPORTED.build("暂不支持该登陆方式"));
        throw new BusinessException(ResponseCode.APPLICATION_NOT_SUPPORTED, "暂不支持该登陆方式");
    }

    private void insertSignInLog(SignInLog log, ResponseCode code) {
        ResultUtil.buildResult(log, code);
        signInLogMapper.insertSelective(log);
    }

}
