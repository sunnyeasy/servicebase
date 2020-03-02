package com.easy.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisCluster implements RedisClient {
    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private String prefix;
    private JedisCluster cluster;
    private RedisConfig config;

    public RedisCluster(String prefix, RedisConfig config) {
        this.prefix = prefix + ":";
        this.config = config;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(config.getMaxIdle());
        poolConfig.setMinIdle(config.getMinIdle());
        poolConfig.setTestOnReturn(config.getTestOnReturn());
        poolConfig.setTestOnBorrow(config.getTestOnBorrow());

        Set<HostAndPort> nodes = new HashSet<>();
        String[] values = config.getSharding().split(";");
        for (int i = 0; i < values.length; i++) {
            String[] hp = values[i].split(":");
            HostAndPort h = new HostAndPort(hp[0], Integer.valueOf(hp[1]));
            nodes.add(h);
        }

        if (StringUtils.isEmpty(config.getPassword())) {
            cluster = new JedisCluster(nodes, config.getConnectTimeout(), config.getSoTimeout(), config.getMaxActive(), poolConfig);
        } else {
            cluster = new JedisCluster(nodes, config.getConnectTimeout(), config.getSoTimeout(), config.getMaxActive(), config.getPassword(), poolConfig);
        }

    }

    @Override
    public String getPreFix() {
        return prefix;
    }

    @Override
    public String getRedisKey(String key) {
        return prefix + key;
    }

    /***
     * string
     ***/
    @Override
    public String get(String key) {
        String k = getRedisKey(key);
        return cluster.get(k);
    }

    @Override
    public String set(String key, String value) {
        String k = getRedisKey(key);
        return cluster.set(k, value);
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        String k = getRedisKey(key);
        return cluster.hmset(k, hash);
    }

    @Override
    public Long hset(String key, String field, String value) {
        String k = getRedisKey(key);
        return cluster.hset(k, field, value);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        String k = getRedisKey(key);
        return cluster.hgetAll(k);
    }

    @Override
    public Long hincrBy(String key, String field, Long value) {
        String k = getRedisKey(key);
        return cluster.hincrBy(k, field, value);
    }

    @Override
    public Long hdel(String key, String field) {
        String k = getRedisKey(key);
        return cluster.hdel(k, field);
    }

    /***
     * sorted set
     ***/
    @Override
    public void zadd(String key, double score, String member) {
        String k = getRedisKey(key);
        cluster.zadd(k, score, member);
    }

    @Override
    public Object eval(String lua, List<String> keys, List<String> args) {
        return cluster.eval(lua, keys, args);
    }

}
