package com.easy.common.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler {
    private Object target;

    private Method method;

    public Method getMethod() {
        return method;
    }

    public static Handler valueOf(Object target, Method method) {
        Handler invoker = new Handler();
        invoker.target = target;
        invoker.method = method;
        return invoker;
    }

    public Object invoke(Object... params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, params);
    }
}
