package com.easy.common.redis;

import java.util.Map;

public interface RedisClient {
    /***
     * string
     ***/
    String get(String key);

    String set(String key, String value);

    /***
     * hash
     ***/
    String hmset(String key, Map<String, String> hash);

    Long hset(String key, String field, String value);

    Long hincrBy(String key, String field, Long value);

    Long hdel(String key, String field);

    /***
     * sorted set
     ***/
    void zadd(String key, double score, String member);

}