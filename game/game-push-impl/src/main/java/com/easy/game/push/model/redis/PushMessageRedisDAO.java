package com.easy.game.push.model.redis;

import com.alibaba.fastjson.JSON;
import com.easy.common.network.packet.push.PushMessage;
import com.easy.common.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public List<PushMessage> getActivePushMessage(long uid, long ttl) {
        List<PushMessage> messages = new ArrayList<>();

        long current = System.currentTimeMillis();

        String qk = getQueueKey(uid);
        Map<String, String> maps = client.hgetAll(qk);

        Iterator<Map.Entry<String, String>> iterator = maps.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            String messageId = entry.getKey();
            long createTime = Long.valueOf(entry.getValue());

            if (current - createTime > ttl) {
                //TODO:启动迁移任务
                continue;
            }

            String k = getKey(uid, Long.valueOf(messageId));
            Map<String, String> values = client.hgetAll(k);
            PushMessage message = JSON.parseObject(JSON.toJSONString(values), PushMessage.class);
            messages.add(message);
        }
        return messages;
    }

    private String getKey(long uid, long messageId) {
        return "pushMessage:" + uid + ":" + messageId;
    }

    private String getQueueKey(long uid) {
        return "pushQueue:" + uid;
    }
}
