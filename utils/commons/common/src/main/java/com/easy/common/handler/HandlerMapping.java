package com.easy.common.handler;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface HandlerMapping {
    String value();
}
