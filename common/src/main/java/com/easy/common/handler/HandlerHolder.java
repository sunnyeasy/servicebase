package com.easy.common.handler;

import com.alibaba.fastjson.JSON;
import com.easy.common.code.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.network.packet.RpcRequest;
import com.easy.common.network.packet.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class HandlerHolder {
    private static final Logger logger = LoggerFactory.getLogger(HandlerHolder.class);

    private static Map<String, Handler> handlerMap = new HashMap<>();

    public static void addHandler(String url, Handler handler) {
        Handler old = handlerMap.get(url);
        if (null != old) {
            logger.error("致命错误, url重复, url={}", url);
            System.exit(0);
        }
        handlerMap.put(url, handler);
    }

    public static Handler getHandler(String url) {
        return handlerMap.get(url);
    }

    public static RpcResponse handle(RpcRequest request) {
        RpcResponse response = new RpcResponse();
        response.setParams(request.getParams());
        try {
            Object data = doHandle(request);
            if (null == data) {
                logger.error("Handle必须返回结果, url={}", request.getUrl());
                throw new BusinessException(ResponseCode.HANDLER_DEFINED_ERROR);
            }
            response.setData(JSON.toJSONString(data));
        } catch (Exception e) {
            logger.error("执行请求异常, url={}", request.getUrl(), e);
            response.setCode(BusinessException.parseResponseCode(e));
        }
        return response;
    }

    private static Object doHandle(RpcRequest request) throws InvocationTargetException, IllegalAccessException {
        String url = request.getUrl();
        Handler handler = getHandler(url);
        if (null == handler) {
            throw new BusinessException(ResponseCode.URL_ERROR);
        }

        if ("void".equals(handler.getMethod().getReturnType().getName())) {
            throw new BusinessException(ResponseCode.HANDLER_DEFINED_ERROR);
        }

        Object[] args = ParametersParser.parse(handler, request);
        Object result = handler.invoke(args);

        return result;
    }
}
