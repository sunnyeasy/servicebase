package com.easy.common.redis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisClientTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisClientTest.class);

    @Test
    public void get() {
        RedisConfig config = new RedisConfig();
        config.setMaxIdle(10000);
        config.setMinIdle(5000);
        config.setTestOnReturn(true);
        config.setTestOnBorrow(true);
        config.setSharding("192.168.0.108:6379");
        config.setConnectTimeout(20000);
        config.setSoTimeout(2000);
        config.setPassword(null);
        config.setMaxActive(100);
        config.setCluster(false);

        RedisClient client = RedisClientFactory.create("common_test", config);
        String v = client.set("test", "12345");
        logger.info("v={}", v);

        v = client.get("test");
        logger.info("v={}", v);
    }
}
