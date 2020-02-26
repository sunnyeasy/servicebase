package com.easy.user.rpcimpl;

import com.easy.common.handler.HandlerHolder;
import com.easy.common.transport.packet.gateway.RpcRequest;
import com.easy.common.transport.packet.gateway.RpcResponse;
import com.easy.gateway.rpcapi.GatewayRpcService;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;

@MotanService
public class GatewayRpcServiceImpl implements GatewayRpcService {

    @Override
    public RpcResponse route(RpcRequest request) {
        return HandlerHolder.handle(request);
    }
}
