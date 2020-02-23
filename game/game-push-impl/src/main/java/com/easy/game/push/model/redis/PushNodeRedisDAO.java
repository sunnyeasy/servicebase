package com.easy.game.push.model.redis;

import com.alibaba.fastjson.JSON;
import com.easy.common.redis.RedisClient;
import com.easy.push.registry.zookeeper.PushNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushNodeRedisDAO {
    private static final Logger logger = LoggerFactory.getLogger(PushNodeRedisDAO.class);

    @Autowired
    private RedisClient client;

    public PushNode get(Long uid) {
        String k = getKey(uid);
        String v = client.get(k);
        if (StringUtils.isEmpty(v)) {
            logger.error("Redis data is not there. key={}", k);
            return null;
        }

        return JSON.parseObject(v, PushNode.class);
    }

    public void set(Long uid, PushNode node) {
        String k = getKey(uid);
        client.set(k, JSON.toJSONString(node));
    }

    private String getKey(Long uid) {
        return "pushNode:" + uid;
    }
}
