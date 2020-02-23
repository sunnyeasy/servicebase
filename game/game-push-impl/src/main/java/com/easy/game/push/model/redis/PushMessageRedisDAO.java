package com.easy.game.push.model.redis;

import com.easy.common.network.packet.push.PushMessage;
import com.easy.common.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushMessageRedisDAO {
    private static final Logger logger = LoggerFactory.getLogger(PushMessageRedisDAO.class);

    @Autowired
    private RedisClient client;

    public void save(PushMessage message) {
        String k = getKey(message.getUid(), message.getMessageId());
        client.hmset(k, message.toMap());

        k = getQueueKey(message.getUid());
        client.hset(k, String.valueOf(message.getMessageId()), String.valueOf(message.getCreateTime()));
    }

    public void update(PushMessage message) {
        String k = getKey(message.getUid(), message.getMessageId());
        client.hmset(k, message.toUpdateMap());
    }

    public void incrPushCount(PushMessage message) {
        String k = getKey(message.getUid(), message.getMessageId());
        client.hincrBy(k, "pushCount", 1L);
    }

    public void deletePushQueue(long uid, long messageId) {
        String k = getQueueKey(uid);
        client.hdel(k, String.valueOf(messageId));
    }

    private String getKey(long uid, long messageId) {
        return "pushMessage:" + uid + ":" + messageId;
    }

    private String getQueueKey(long uid) {
        return "pushQueue:" + uid;
    }
}
