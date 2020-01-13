package com.easy.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Map;

public class HandlerInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInitializer.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("开始注册HandlerMapping");

        Map<String, Object> beanMap = event.getApplicationContext().getBeansWithAnnotation(HandlerMapping.class);

        for (Object bean : beanMap.values()) {
            HandlerMapping typeMapping = bean.getClass().getAnnotation(HandlerMapping.class);

            Method[] methods = bean.getClass().getMethods();
            if (null == methods || methods.length < 1) {
                continue;
            }

            for (Method method : methods) {
                HandlerMapping methodMapping = method.getClass().getAnnotation(HandlerMapping.class);
                if (null == methodMapping) {
                    continue;
                }

                String url = buildUrl(typeMapping.value(), methodMapping.value());
                logger.info("注册HandlerMapping, url={}, class={}, method={}", url, bean.getClass().getTypeName(), method.getName());

                Handler handler = Handler.valueOf(bean, method);
                HandlerHolder.addHandler(url, handler);
            }
        }

        logger.info("完成注册HandlerMapping");
    }

    private String buildUrl(String typeUrl, String methodUrl) {
        if (StringUtils.isEmpty(methodUrl)) {
            logger.error("url错误, typeUrl={}, methodUrl={}", typeUrl, methodUrl);
            System.exit(0);
            return null;
        }

        if (StringUtils.isEmpty(typeUrl)) {
            if (methodUrl.startsWith("/")) {
                return methodUrl;
            }

            return "/" + methodUrl;
        }

        if (typeUrl.endsWith("/") && methodUrl.startsWith("/")) {
            return typeUrl + methodUrl.substring(1, methodUrl.length() - 1);
        }

        if (!typeUrl.endsWith("/") && !methodUrl.startsWith("/")) {
            return typeUrl + "/" + methodUrl;
        }

        return typeUrl + methodUrl;
    }
}
