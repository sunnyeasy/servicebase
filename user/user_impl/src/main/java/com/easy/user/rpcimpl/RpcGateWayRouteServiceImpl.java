package com.easy.user.rpcimpl;

import com.easy.common.handler.HandlerHolder;
import com.easy.common.network.RpcGateWayRouteService;
import com.easy.common.network.packet.RpcRequest;
import com.easy.common.network.packet.RpcResponse;

public class RpcGateWayRouteServiceImpl implements RpcGateWayRouteService {

    @Override
    public RpcResponse route(RpcRequest request) {
        return HandlerHolder.handle(request);
    }
}
