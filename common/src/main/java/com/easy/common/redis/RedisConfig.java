package com.easy.common.redis;

import org.springframework.core.env.Environment;

public class RedisConfig {
    private Integer maxIdle;
    private Integer minIdle;
    private Boolean testOnReturn;
    private Boolean testOnBorrow;
    private String sharding;
    private Integer connectTimeout;
    private Integer soTimeout;
    private String password;
    private Integer maxActive;
    private Boolean cluster;

    public static RedisConfig build(Environment env) {
        RedisConfig config = new RedisConfig();

        String p = env.getProperty("redis.maxIdle", "100");
        config.setMaxIdle(Integer.valueOf(p));

        p = env.getProperty("redis.minIdle", "10");
        config.setMinIdle(Integer.valueOf(p));

        p = env.getProperty("redis.testOnReturn", "true");
        config.setTestOnReturn(Boolean.valueOf(p));

        p = env.getProperty("redis.testOnBorrow", "true");
        config.setTestOnBorrow(Boolean.valueOf(p));

        p = env.getProperty("redis.sharing");
        config.setSharding(p);

        p = env.getProperty("redis.connectTimeout", "2000");
        config.setConnectTimeout(Integer.valueOf(p));

        p = env.getProperty("redis.soTimeout", "2000");
        config.setSoTimeout(Integer.valueOf(p));

        p = env.getProperty("redis.password");
        config.setPassword(p);

        p = env.getProperty("redis.maxActive", "10");
        config.setMaxActive(Integer.valueOf(p));

        p = env.getProperty("redis.cluster", "true");
        config.setCluster(Boolean.valueOf(p));

        return config;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getSharding() {
        return sharding;
    }

    public void setSharding(String sharding) {
        this.sharding = sharding;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Boolean getCluster() {
        return cluster;
    }

    public void setCluster(Boolean cluster) {
        this.cluster = cluster;
    }
}
