package com.easy.game.gateway;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.lang.MethodResult;
import com.easy.common.lang.MethodReturn2;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.common.transport.packet.gateway.RpcRequest;
import com.easy.common.transport.packet.gateway.RpcResponse;
import com.easy.gateway.GatewayMotanClient;
import com.easy.gateway.transport.netty4.AppResponseBuilderV2;
import com.easy.gateway.transport.netty4.GatewayListener;
import com.easy.gateway.transport.netty4.HttpRequest;
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
    public FullHttpResponse route(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        AppRequest appRequest = JSON.parseObject(httpRequest.getBody(), AppRequest.class);
        appRequest.setIp(httpRequest.getIp());

        //身份认证
        MethodReturn2<FullHttpResponse, Long> authReturn = auth(appRequest);
        if (!authReturn.getResult().equals(MethodResult.successful)) {
            return authReturn.getData();
        }

        Long uid = authReturn.getData2();
        appRequest.setUid(uid);

        //路由消息
        FullHttpResponse response = route(appRequest, httpRequest);
        return response;
    }

    private MethodReturn2<FullHttpResponse, Long> auth(AppRequest appRequest) {
        MethodReturn2<FullHttpResponse, Long> return2 = new MethodReturn2<>();

        if (GameGatewayUrlConfig.isNoAuthUrl(appRequest.getUrl())) {
            return return2;
        }

        FullHttpResponse response = null;
        Long uid = null;

        if (StringUtils.isEmpty(appRequest.getToken())) {
            logger.error("Token is invalid, appRequest={}", JSON.toJSONString(appRequest));
            response = AppResponseBuilderV2.build(ResponseCode.UNAUTHORIZED);

            return2.setData(response);
            return2.setResult(MethodResult.fail);
            return return2;
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
            if (null != authRpcVo) {
                code.setReason(authRpcVo.getCode().getMessage());
            }

            response = AppResponseBuilderV2.build(code);

            return2.setData(response);
            return2.setResult(MethodResult.fail);
            return return2;
        }

        uid = authRpcVo.getUid();
        return2.setData2(uid);
        return return2;
    }

    private FullHttpResponse route(AppRequest appRequest, HttpRequest httpRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("request body={}", httpRequest.getBody());
        }

        FullHttpResponse response = null;

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setUid(appRequest.getUid());
        rpcRequest.setUrl(appRequest.getUrl());
        rpcRequest.setData(httpRequest.getBody());
        rpcRequest.setParams(appRequest.getParams());

        RpcResponse rpcResponse = gatewayMotanClient.route(httpRequest.getUri(), rpcRequest);

        if (RpcResponse.isFail(rpcResponse)) {
            logger.error("Route request fail, appRequest={}, rpcResponse={}", JSON.toJSONString(appRequest), JSON.toJSONString(rpcResponse));

            ResponseCode code = null;
            if (null == rpcResponse) {
                code = ResponseCode.UNKNOWN_ERROR;
            } else if (rpcResponse.getCode().getErrorCode() == ResponseCode.URL_ERROR.getErrorCode()) {
                code = ResponseCode.NOT_FOUND;
            } else {
                code = rpcResponse.getCode();
            }

            response = AppResponseBuilderV2.build(code);
        } else {
            response = AppResponseBuilderV2.build(rpcResponse.getCode(), rpcResponse.getData(), rpcResponse.getParams());
        }
        return response;
    }
}
