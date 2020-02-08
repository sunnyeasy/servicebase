package com.easy.common.network;

import com.easy.common.network.packet.gateway.RpcRequest;
import com.easy.common.network.packet.gateway.RpcResponse;

public interface GateWayRpcRouteService {
    String VERSION = "1.0";

    RpcResponse route(RpcRequest request);
}
