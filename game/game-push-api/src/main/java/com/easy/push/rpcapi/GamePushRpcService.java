package com.easy.push.rpcapi;

import com.easy.common.network.packet.push.RpcPushRequest;
import com.easy.common.rpcvo.BaseRpcVo;

public interface GamePushRpcService {
    BaseRpcVo push(RpcPushRequest request);
}
