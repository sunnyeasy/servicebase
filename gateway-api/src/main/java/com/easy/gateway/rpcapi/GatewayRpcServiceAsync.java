package com.easy.gateway.rpcapi;

import com.easy.common.transport.packet.gateway.RpcRequest;
import com.weibo.api.motan.rpc.ResponseFuture;

public interface GatewayRpcServiceAsync extends GatewayRpcService {
    ResponseFuture routeAsync(RpcRequest request);

}
