package com.easy.user.service.impl;

import com.easy.common.enums.model.CommonEnums;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.dao.SignUpLogMapper;
import com.easy.user.model.mysql.dao.SignUpMapper;
import com.easy.user.model.mysql.dao.UserMapper;
import com.easy.user.model.mysql.po.SignUp;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.model.mysql.po.User;
import com.easy.user.model.mysql.po.builder.SignUpBuilder;
import com.easy.user.model.mysql.po.builder.SignUpLogBuilder;
import com.easy.user.model.mysql.po.builder.UserBuilder;
import com.easy.user.service.SignUpService;
import com.easy.user.vo.SignUpVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SignUpServiceImpl implements SignUpService {
    private static final Logger logger = LoggerFactory.getLogger(SignUpServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignUpMapper signUpMapper;
    @Autowired
    private SignUpLogMapper signUpLogMapper;

    @Override
    public SignUpVo signUpWithTX(AppRequest req, SignUpAo ao, SignUpLog signUpLog) {
        User user = UserBuilder.build(ao, signUpLog);
        userMapper.insertSelective(user);

        User referrerUser = userMapper.selectByMobile(signUpLog.getReferrerMobile());
        if (null != referrerUser) {
            signUpLog.setReferrerUid(referrerUser.getUid());
        }

        SignUp signUp = SignUpBuilder.build(user, signUpLog);
        signUpMapper.insertSelective(signUp);

        SignUpLog updateLog = SignUpLogBuilder.update(signUpLog, user.getUid(), CommonEnums.Result.successful);
        signUpLogMapper.updateByPrimaryKeySelective(updateLog);

        SignUpVo vo = new SignUpVo();
        vo.setUid(user.getUid());
        return vo;
    }
}
