package com.easy.user.handler;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.handler.HandlerMapping;
import com.easy.common.rpcvo.BaseRecordRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.constant.model.enums.CommonEnums;
import com.easy.errorcode.UserResponseCode;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.dao.SignUpLogMapper;
import com.easy.user.model.mysql.dao.UserMapper;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.model.mysql.po.User;
import com.easy.user.model.mysql.po.builder.SignUpLogBuilder;
import com.easy.user.service.SignUpService;
import com.easy.user.vo.SignUpVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@HandlerMapping("/user")
public class SignUpHandler {
    private static final Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignUpLogMapper signUpLogMapper;

    @Autowired
    private SignUpService signUpService;

    @HandlerMapping("/signUp")
    public BaseRecordRpcVo<SignUpVo> signUp(AppRequest req, SignUpAo ao) {
        logger.info("ao={}", JSON.toJSONString(ao));

        SignUpLog signUpLog = SignUpLogBuilder.build(ao, req);

        //注册校验
        if (StringUtils.isEmpty(ao.getMobile())) {
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "手机号错误");
        }

        User user = userMapper.selectByMobile(ao.getMobile());
        if (null != user) {
            insertSignUpLog(signUpLog, UserResponseCode.MOBILE_SIGNED_UP);
            throw new BusinessException(UserResponseCode.MOBILE_SIGNED_UP);
        }

        if (StringUtils.isEmpty(ao.getPassword())) {
            insertSignUpLog(signUpLog, ResponseCode.PARAMETERS_ERROR.build("密码错误"));
            throw new BusinessException(ResponseCode.PARAMETERS_ERROR, "密码错误");
        }

        signUpLog.setResult(CommonEnums.Result.wait.getCode());
        signUpLogMapper.insertSelective(signUpLog);

        BaseRecordRpcVo<SignUpVo> rpcVo = null;
        try {
            rpcVo = signUpService.signUpWithTX(req, ao, signUpLog);
        } catch (Exception e) {
            logger.error("用户注册失败, ao={}", JSON.toJSONString(ao), e);
            ResponseCode code = BusinessException.parseResponseCode(e);

            SignUpLog updateLog = new SignUpLog();
            updateLog.setId(signUpLog.getId());
            SignUpLogBuilder.buildResult(updateLog, code);
            updateLog.setUpdateTime(new Date());
            signUpLogMapper.updateByPrimaryKeySelective(updateLog);

            rpcVo = new BaseRecordRpcVo<>();
            rpcVo.setCode(code);
        }
        return rpcVo;
    }

    private void insertSignUpLog(SignUpLog log, ResponseCode code) {
        SignUpLogBuilder.buildResult(log, code);
        signUpLogMapper.insertSelective(log);
    }
}
