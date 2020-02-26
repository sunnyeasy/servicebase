package com.easy.gateway.rpcapi;

import com.easy.common.transport.packet.gateway.RpcRequest;
import com.easy.common.transport.packet.gateway.RpcResponse;

public interface GatewayRpcService {
    String VERSION = "1.0";

    RpcResponse route(RpcRequest request);
}
