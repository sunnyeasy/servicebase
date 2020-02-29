package com.easy.common.redis;

public class RedisClientFactory {
    public static RedisClient create(String prefix, RedisConfig config) {
        if (config.getCluster()) {
            return new RedisCluster(prefix, config);
        } else {
            return new Redis(prefix, config);
        }
    }
}
