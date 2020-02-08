package com.easy.common.network;

import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;

public interface PushRpcService {
    String VERSION="1.0";

    AuthRpcVo auth(AuthRpcAo ao);
}
