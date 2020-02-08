package com.easy.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

public class Redis implements RedisClient {
    private static final Logger logger = LoggerFactory.getLogger(Redis.class);
    private String prefix;
    private JedisPool pool;
    private RedisConfig config;

    public Redis(String prefix, RedisConfig config) {
        this.prefix = prefix + ":";
        this.config = config;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(config.getMaxIdle());
        poolConfig.setMinIdle(config.getMinIdle());
        poolConfig.setTestOnReturn(config.getTestOnReturn());
        poolConfig.setTestOnBorrow(config.getTestOnBorrow());

        HostAndPort node = null;
        String[] values = config.getSharding().split(";");
        for (int i = 0; i < values.length; i++) {
            String[] hp = values[i].split(":");
            node = new HostAndPort(hp[0], Integer.valueOf(hp[1]));
            break;
        }

        if (StringUtils.isEmpty(config.getPassword())) {
            pool = new JedisPool(poolConfig, node.getHost(), node.getPort(), 10000);
        } else {
            pool = new JedisPool(poolConfig, node.getHost(), node.getPort(), 10000, config.getPassword());
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = pool.getResource();
        String k = getRedisKey(key);
        try {
            return jedis.get(k);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = pool.getResource();
        String k = getRedisKey(key);
        try {
            return jedis.set(k, value);
        } finally {
            jedis.close();
        }
    }

    private String getRedisKey(String key) {
        return prefix + key;
    }

}
