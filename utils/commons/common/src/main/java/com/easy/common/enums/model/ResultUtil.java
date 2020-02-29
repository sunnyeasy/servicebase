package com.easy.common.enums.model;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ResultUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResultUtil.class);

    public static void buildResult(Object object, ResponseCode code) {
        try {
            doBuildResult(object, code);
        } catch (Exception e) {
            logger.error("Build po result exception. object={}", JSON.toJSONString(object), e);
        }
    }

    private static void doBuildResult(Object object, ResponseCode code) throws IllegalAccessException {
        if (code.getErrorCode() == ResponseCode.SUCESSFUL.getErrorCode()) {
            return;
        }

        set(object, "result", CommonEnums.Result.fail.getCode());
        set(object, "failCode", String.valueOf(code.getErrorCode()));
        set(object, "failReason", code.failReason());
    }

    private static void set(Object object, String name, Object value) throws IllegalAccessException {
        Class cls = object.getClass();
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return;
        }

        field.setAccessible(true);
        field.set(object, value);
    }
}
