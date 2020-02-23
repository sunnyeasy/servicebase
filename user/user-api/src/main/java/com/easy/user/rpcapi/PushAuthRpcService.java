package com.easy.user.rpcapi;

import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.weibo.api.motan.transport.async.MotanAsync;

public interface PushAuthRpcService {
    String VERSION="1.0";

    AuthRpcVo auth(AuthRpcAo ao);
}
