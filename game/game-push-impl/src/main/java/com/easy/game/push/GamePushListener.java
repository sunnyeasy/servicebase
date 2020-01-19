package com.easy.game.push;

import com.alibaba.fastjson.JSON;
import com.easy.common.network.NetworkConstants;
import com.easy.common.network.packet.PushMessage;
import com.easy.common.network.packet.RpcPushRequest;
import com.easy.push.transport.netty4.*;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GamePushListener implements MqttListener, MqttClusterListener {
    private static final Logger logger = LoggerFactory.getLogger(GamePushListener.class);

    private Map<String, Long> clientIdToUidMap = new ConcurrentHashMap<>();
    private Map<Long, String> uidToClientIdMap = new ConcurrentHashMap<>();

    private static final String GAME_PUSH_TOPIC = "/game/server/to/client/";

    @Override
    public void channelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception {
        channel.setAuth(true);

        Long uid = 1000000L;
        clientIdToUidMap.put(channel.getClientId(), uid);
        uidToClientIdMap.put(uid, channel.getClientId());

        router.channelAuth(channel);
    }

    @Override
    public void finishPushMessage(PushMessage message) {
        logger.info("推送消息结束, message={}", JSON.toJSONString(message));
    }

    @Override
    public void clusterChannelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception {
        channel.setAuth(true);

        router.channelAuth(channel);
    }

    @Override
    public PushMessage pushMessage(MqttPublishMessage message) throws Exception {
        ByteBuf payload=message.payload();
        byte[] data = new byte[payload.readableBytes()];
        int mark=payload.readerIndex();
        payload.readBytes(data);
        payload.readerIndex(mark);

        RpcPushRequest request = JSON.parseObject(new String(data, NetworkConstants.UTF8), RpcPushRequest.class);

        String clientId = uidToClientIdMap.get(request.getUid());
        if (null == clientId) {
            logger.error("Client has not been authed on this node, or logined on other node. uid={}", request.getUid());
            return null;
        }

        PushMessage pushMessage = new PushMessage();
        pushMessage.setClientId(clientId);

        String topic = GAME_PUSH_TOPIC + request.getUid();
        pushMessage.setTopic(topic);

        pushMessage.setMqttMessageId(100);
        pushMessage.setUid(request.getUid());
        pushMessage.setData(request.getData());
        return pushMessage;
    }
}
