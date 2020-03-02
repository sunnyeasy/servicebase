package com.easy.common.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.jar.JarEntry;

public class RedisClientTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisClientTest.class);

    public static final String SHARDING = "172.16.1.248:6379";
    public static RedisConfig config;

    static {
        config = new RedisConfig();
        config.setMaxIdle(10000);
        config.setMinIdle(5000);
        config.setTestOnReturn(true);
        config.setTestOnBorrow(true);
        config.setSharding(SHARDING);
        config.setConnectTimeout(20000);
        config.setSoTimeout(2000);
        config.setPassword(null);
        config.setMaxActive(100);
        config.setCluster(false);
    }

    @Test
    public void get() {
        RedisClient client = RedisClientFactory.create("common_test", config);
        String v = client.set("test", "12345");
        logger.info("v={}", v);

        v = client.get("test");
        logger.info("v={}", v);
    }

    @Test
    public void eval() {
        RedisClient client = RedisClientFactory.create("common_test", config);
//        Object object = client.eval("return {KEYS[1],ARGV[1]}", Lists.newArrayList("easy"), Lists.newArrayList("12"));

        String lua="local rtv=redis.call('set',KEYS[1],ARGV[1])\n"
//                +"return rtv\n"
                +"if rtv=='OK' then\n"
                +"\tlocal v=redis.call('get',KEYS[1])\n"
//                +"\treturn v\n"
                +"\treturn 1\n"
                +"end"
                ;

        lua="local rtv=redis.call('hmset',KEYS[1],KEYS[2],ARGV[1],KEYS[3],ARGV[2])\n"
                +"return rtv\n"
                ;

        Object object = client.eval(lua, Lists.newArrayList("easy-hash","name","age"), Lists.newArrayList("easy","12"));
        logger.info("object={}", JSON.toJSONString(object));
    }
}
