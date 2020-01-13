package com.easy.common.network;

import com.easy.common.network.packet.RpcRequest;
import com.easy.common.network.packet.RpcResponse;

public interface RpcGateWayRouteService {
    String VERSION = "1.0";

    RpcResponse route(RpcRequest request);
}
