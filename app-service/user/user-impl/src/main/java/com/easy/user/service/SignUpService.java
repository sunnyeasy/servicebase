package com.easy.user.service;

import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.user.ao.SignUpAo;
import com.easy.user.model.mysql.po.SignUpLog;
import com.easy.user.vo.SignUpVo;

public interface SignUpService {
    SignUpVo signUpWithTX(AppRequest req, SignUpAo ao, SignUpLog signUpLog);
}
