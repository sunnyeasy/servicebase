package com.easy.gateway;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.transport.packet.gateway.RpcRequest;
import com.easy.common.transport.packet.gateway.RpcResponse;
import com.easy.gateway.rpcapi.GatewayRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GatewayMotanClient {
    private static final Logger logger = LoggerFactory.getLogger(GatewayMotanClient.class);

    private Map<String, GatewayRpcService> motanClients = new HashMap<>();

    public void put(String uri, GatewayRpcService service) {
        motanClients.put(uri, service);
    }

    public RpcResponse route(String uri, RpcRequest request) {
        RpcResponse rpcResponse = null;

        GatewayRpcService gatewayRpcService = motanClients.get(uri);
        if (null == gatewayRpcService) {
            logger.error("URI is invalid, uri={}, request={}", uri, JSON.toJSONString(request));
            rpcResponse = new RpcResponse();
            rpcResponse.setCode(ResponseCode.NOT_FOUND);
            return rpcResponse;
        }

        rpcResponse = gatewayRpcService.route(request);
        return rpcResponse;
    }
}
