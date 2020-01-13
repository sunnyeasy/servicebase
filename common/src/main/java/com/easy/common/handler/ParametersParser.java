package com.easy.common.handler;

import com.alibaba.fastjson.JSON;
import com.easy.common.network.packet.RpcRequest;

import java.lang.reflect.Method;

public class ParametersParser {
    public static Object[] parse(Handler handler, RpcRequest request) {
        Method method = handler.getMethod();
        Class<?>[] paramTypes = method.getParameterTypes();

        if (null == paramTypes || 0 == paramTypes.length) {
            return new Object[0];
        }

        Object[] params = new Object[paramTypes.length];
        for (int i=0; i<paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            params[i] = parse(method, paramType, request);
        }
        return params;
    }

    public static Object parse(Method method, Class<?> paramType, RpcRequest request) {
        if (RpcRequest.class.isAssignableFrom(paramType)) {
            return request;
        } else {
            return JSON.parseObject(request.getData(), paramType);
        }
    }
}
