package com.easy.push.rpcapi;

import com.easy.common.transport.packet.push.RpcPushRequest;
import com.easy.common.rpcvo.BaseRpcVo;

public interface GamePushRpcService {
    String VERSION = "1.0";

    BaseRpcVo push(RpcPushRequest request);
}
