package com.easy.game.push.model.redis.dao;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.packet.push.PushMessage;
import com.easy.common.redis.RedisClient;
import com.easy.game.push.model.redis.po.PushMessageBrief;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

        if (CollectionUtils.isEmpty(maps)) {
            return messages;
        }

        List<PushMessageBrief> briefs = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iterator = maps.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            PushMessageBrief brief = new PushMessageBrief();
            brief.setMessageId(entry.getKey());

            try {
                brief.setCreateTime(Long.valueOf(entry.getValue()));
            } catch (Exception e) {
                logger.error("Push message createTime error. messageId={}, createTime={}", entry.getKey(), entry.getValue());
                continue;
            }
            briefs.add(brief);
        }

        Collections.sort(briefs, new Comparator<PushMessageBrief>() {
            @Override
            public int compare(PushMessageBrief o1, PushMessageBrief o2) {
                long v = o1.getCreateTime() - o2.getCreateTime();
                if (0L == v) {
                    return 0;
                } else if (v > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (PushMessageBrief brief : briefs) {
            String messageId = brief.getMessageId();
            long createTime = brief.getCreateTime();

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
