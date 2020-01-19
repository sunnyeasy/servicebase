package com.easy.push.rpcapi;

import com.easy.common.network.packet.RpcPushRequest;
import com.easy.common.rpcVo.BaseRpcVo;

public interface GamePushRpcService {
    BaseRpcVo push(RpcPushRequest request);
}
