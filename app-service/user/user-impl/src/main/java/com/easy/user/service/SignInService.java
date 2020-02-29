package com.easy.user.service;

import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.ao.SignInAo;
import com.easy.user.model.mysql.po.SignInLog;
import com.easy.user.model.mysql.po.User;
import com.easy.user.vo.SignInVo;

public interface SignInService {
    SignInVo signInWithTX(AppRequest req, SignInAo ao, User user,SignInLog signInLog);
}
