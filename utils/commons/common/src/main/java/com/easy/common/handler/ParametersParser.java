package com.easy.common.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.common.transport.packet.gateway.RpcRequest;

import java.lang.reflect.Method;

public class ParametersParser {
    public static Object[] parse(Handler handler, RpcRequest request, AppRequest appRequest) {
        Method method = handler.getMethod();
        Class<?>[] paramTypes = method.getParameterTypes();

        if (null == paramTypes || 0 == paramTypes.length) {
            return new Object[0];
        }

        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            params[i] = parse(method, paramType, request, appRequest);
        }
        return params;
    }

    private static Object parse(Method method, Class<?> paramType, RpcRequest request, AppRequest appRequest) {
        if (RpcRequest.class.isAssignableFrom(paramType)) {
            return request;
        } else if (AppRequest.class.equals(paramType)) {
            return appRequest;
        } else {
            JSONObject root = JSON.parseObject(request.getData());
            String data = root.getString("data");
            Object object = JSONObject.parseObject(data, paramType);
            return object;
        }
    }
}
