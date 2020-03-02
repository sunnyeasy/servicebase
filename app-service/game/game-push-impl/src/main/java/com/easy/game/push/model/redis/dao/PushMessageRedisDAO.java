package com.easy.game.push.model.redis.dao;

import com.alibaba.fastjson.JSON;
import com.easy.common.transport.packet.push.PushMessage;
import com.easy.common.redis.RedisClient;
import com.easy.game.push.model.redis.po.PushMessageBrief;
import org.assertj.core.util.Lists;
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
        doSaveLua(message);
    }

    private void doSave(PushMessage message) {
        String k = getKey(message.getUid(), message.getMessageId());
        client.hmset(k, message.toMap());

        k = getQueueKey(message.getUid());
        client.hset(k, String.valueOf(message.getMessageId()), String.valueOf(message.getCreateTime()));
    }

    private void doSaveLua(PushMessage message) {
        List<String> keys = Lists.newArrayList();
        List<String> args = Lists.newArrayList();

        Map<String, String> map = message.toMap();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            keys.add(entry.getKey());
            args.add(entry.getValue());
        }

        String k = getKey(message.getUid(), message.getMessageId());
        k = client.getRedisKey(k);
        keys.add(k);

        String qk = getQueueKey(message.getUid());
        qk = client.getRedisKey(qk);
        keys.add(qk);
        args.add(String.valueOf(message.getMessageId()));
        args.add(String.valueOf(message.getCreateTime()));

        String lua = "redis.call('hmset',KEYS[13],KEYS[1],ARGV[1],KEYS[2],ARGV[2],KEYS[3],ARGV[3],KEYS[4],ARGV[4],KEYS[5],ARGV[5],KEYS[6],ARGV[6],KEYS[7],ARGV[7],KEYS[8],ARGV[8],KEYS[9],ARGV[9],KEYS[10],ARGV[10],KEYS[11],ARGV[11],KEYS[12],ARGV[12])\n"
                + "redis.call('hset',KEYS[14],ARGV[13],ARGV[14])\n";
        client.eval(lua, keys, args);
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

    public void successPushMessage(PushMessage message) {
        List<String> keys = Lists.newArrayList();
        List<String> args = Lists.newArrayList();

        Map<String, String> map = message.toUpdateMap();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            keys.add(entry.getKey());
            args.add(entry.getValue());
        }

        String k = getKey(message.getUid(), message.getMessageId());
        k = client.getRedisKey(k);
        keys.add(k);

        String qk = getQueueKey(message.getUid());
        qk = client.getRedisKey(qk);
        keys.add(qk);
        args.add(String.valueOf(message.getMessageId()));

        String lua = "redis.call('hmset',KEYS[8],KEYS[1],ARGV[1],KEYS[2],ARGV[2],KEYS[3],ARGV[3],KEYS[4],ARGV[4],KEYS[5],ARGV[5],KEYS[6],ARGV[6],KEYS[7],ARGV[7])\n"
                + "redis.call('hdel',KEYS[9],ARGV[8])\n";
        client.eval(lua, keys, args);
    }

    public void preSuccessPushMessage(PushMessage message) {
        List<String> keys = Lists.newArrayList();
        List<String> args = Lists.newArrayList();

        Map<String, String> map = message.toUpdateMap();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            keys.add(entry.getKey());
            args.add(entry.getValue());
        }

        String k = getKey(message.getUid(), message.getMessageId());
        k = client.getRedisKey(k);
        keys.add(k);

        String lua = "redis.call('hmset',KEYS[8],KEYS[1],ARGV[1],KEYS[2],ARGV[2],KEYS[3],ARGV[3],KEYS[4],ARGV[4],KEYS[5],ARGV[5],KEYS[6],ARGV[6],KEYS[7],ARGV[7])\n"
                + "redis.call('hincrby',KEYS[8],'pushCount','1')\n";
        client.eval(lua, keys, args);
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
