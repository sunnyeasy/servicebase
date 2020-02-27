package com.easy.common.model;

import org.springframework.stereotype.Component;

@Component
public class DataSourceConfig {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String filters;
    private String initialSize;
    private String maxActive;
    private String minIdle;
    private String maxWait;
    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;
    private String validationQuery;
    private String testOnBorrow;
    private String testOnReturn;
    private String removeAbandoned;
    private String removeAbandonedTimeout;
    private String logAbandoned;
    private String testWhileIdle;

    
}
