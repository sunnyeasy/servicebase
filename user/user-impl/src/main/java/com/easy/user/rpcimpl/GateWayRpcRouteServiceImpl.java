package com.easy.user.rpcimpl;

import com.easy.common.handler.HandlerHolder;
import com.easy.common.network.GateWayRpcRouteService;
import com.easy.common.network.packet.gateway.RpcRequest;
import com.easy.common.network.packet.gateway.RpcResponse;

public class GateWayRpcRouteServiceImpl implements GateWayRpcRouteService {

    @Override
    public RpcResponse route(RpcRequest request) {
        return HandlerHolder.handle(request);
    }
}
