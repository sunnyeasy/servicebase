package com.easy.game.gateway;

import com.alibaba.fastjson.JSON;
import com.easy.common.code.ResponseCode;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.common.transport.packet.gateway.RpcRequest;
import com.easy.common.transport.packet.gateway.RpcResponse;
import com.easy.gateway.GatewayMotanClient;
import com.easy.gateway.transport.netty4.AppResponseBuilder;
import com.easy.gateway.transport.netty4.GatewayListener;
import com.easy.user.rpcapi.AuthRpcService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameGatewayListener implements GatewayListener {
    private static final Logger logger = LoggerFactory.getLogger(GameGatewayListener.class);

    @Autowired
    private AuthRpcService authRpcService;
    @Autowired
    private GatewayMotanClient gatewayMotanClient;

    @Override
    public FullHttpResponse route(ChannelHandlerContext ctx, String uri, String body) {
        FullHttpResponse response = null;

        AppRequest appRequest = JSON.parseObject(body, AppRequest.class);

        //token校验
        if (StringUtils.isEmpty(appRequest.getToken())) {
            logger.error("Token is invalid, appRequest={}", JSON.toJSONString(appRequest));
            response = AppResponseBuilder.build(ResponseCode.UNAUTHORIZED);
            return response;
        }
        AuthRpcAo authRpcAo = new AuthRpcAo();
        authRpcAo.setToken(appRequest.getToken());
        authRpcAo.setBusinessType(appRequest.getBusinessType());
        authRpcAo.setAgentMode(appRequest.getAgentMode());
        authRpcAo.setDeviceId(appRequest.getDeviceId());
        AuthRpcVo authRpcVo = authRpcService.auth(authRpcAo);
        if (BaseRpcVo.isFail(authRpcVo)) {
            logger.error("Token is unauthorized, appRequest={}, authRpcVo={}", JSON.toJSONString(appRequest), JSON.toJSONString(authRpcVo));

            ResponseCode code = new ResponseCode(ResponseCode.UNAUTHORIZED);
            code.setReason(authRpcVo.getCode().getMessage());

            response = AppResponseBuilder.build(code);
            return response;
        }

        //调用服务
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setUid(authRpcVo.getUid());
        rpcRequest.setUrl(appRequest.getUrl());
        rpcRequest.setData(JSON.toJSONString(appRequest));
        rpcRequest.setParams(appRequest.getParams());

        RpcResponse rpcResponse = gatewayMotanClient.route(uri, rpcRequest);

        if (RpcResponse.isFail(rpcResponse)) {
            logger.error("Route request fail, appRequest={}, authRpcVo={}", JSON.toJSONString(appRequest), JSON.toJSONString(authRpcVo));

            ResponseCode code = null;
            if (null == rpcResponse) {
                code = ResponseCode.UNKNOWN_ERROR;
            } else if (rpcResponse.getCode().getCode() == ResponseCode.URL_ERROR.getCode()) {
                code = ResponseCode.NOT_FOUND;
            } else {
                code = rpcResponse.getCode();
            }

            response = AppResponseBuilder.build(code);
            return response;
        }

        response = AppResponseBuilder.build(rpcResponse.getCode(), rpcResponse.getData(), rpcResponse.getParams());
        return response;
    }
}
