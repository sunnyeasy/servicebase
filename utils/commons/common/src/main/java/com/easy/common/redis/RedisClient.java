package com.easy.common.redis;

import java.util.List;
import java.util.Map;

public interface RedisClient {
    String getPreFix();
    String getRedisKey(String key);
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

    Map<String, String> hgetAll(String key);

    Long hincrBy(String key, String field, Long value);

    Long hdel(String key, String field);

    /***
     * sorted set
     ***/
    void zadd(String key, double score, String member);

    Object eval(String lua, List<String> keys, List<String> args);
}