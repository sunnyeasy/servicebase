package com.easy.push.rpcapi;

import com.easy.common.transport.packet.push.RpcPushRequest;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.push.rpcao.PushServerRpcAo;
import com.easy.push.rpcvo.PushServerRpcVo;

public interface GamePushRpcService {
    String VERSION = "1.0";

    BaseRpcVo push(RpcPushRequest request);

    PushServerRpcVo queryPushServer(PushServerRpcAo rpcAo);
}
