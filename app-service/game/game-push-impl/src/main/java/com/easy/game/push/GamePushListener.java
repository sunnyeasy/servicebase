package com.easy.game.push;

import com.alibaba.fastjson.JSON;
import com.easy.common.enums.AgentMode;
import com.easy.common.enums.BusinessType;
import com.easy.common.rpcao.AuthRpcAo;
import com.easy.common.rpcvo.AuthRpcVo;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.NetworkConstants;
import com.easy.common.transport.packet.gateway.AppRequest;
import com.easy.common.transport.packet.push.PushMessage;
import com.easy.game.push.model.redis.dao.PushMessageRedisDAO;
import com.easy.game.push.model.redis.dao.PushNodeRedisDAO;
import com.easy.push.registry.zookeeper.PushNode;
import com.easy.push.transport.netty4.MqttChannel;
import com.easy.push.transport.netty4.MqttClusterListener;
import com.easy.push.transport.netty4.MqttListener;
import com.easy.push.transport.netty4.Router;
import com.easy.user.rpcapi.AuthRpcServiceAsync;
import com.weibo.api.motan.rpc.Future;
import com.weibo.api.motan.rpc.FutureListener;
import com.weibo.api.motan.rpc.ResponseFuture;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GamePushListener implements MqttListener, MqttClusterListener {
    private static final Logger logger = LoggerFactory.getLogger(GamePushListener.class);
    private static final long PUSH_MESSAGE_TTL = 60 * 60 * 1000L;

    private Map<String, Long> clientIdToUidMap = new ConcurrentHashMap<>();
    private Map<Long, String> uidToClientIdMap = new ConcurrentHashMap<>();

    @Autowired
    private AuthRpcServiceAsync authRpcServiceAsync;
    @Autowired
    private PushMessageRedisDAO pushMessageRedisDAO;
    @Autowired
    private PushNodeRedisDAO pushNodeRedisDAO;
    @Autowired
    private PushNode pushNode;

    @Override
    public void channelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception {
        AppRequest appRequest = JSON.parseObject(message.payload().password(), AppRequest.class);

        AuthRpcAo authRpcAo = new AuthRpcAo();
        authRpcAo.setToken(appRequest.getToken());
        authRpcAo.setBusinessType(appRequest.getBusinessType());
        authRpcAo.setAgentMode(appRequest.getAgentMode());
        authRpcAo.setDeviceId(appRequest.getDeviceId());
        authRpcAo.setImei(appRequest.getImei());
        authRpcAo.setVersion(appRequest.getVersion());

        logger.info("User rpc auth, authRpcAo={}", JSON.toJSONString(authRpcAo));

        ResponseFuture future = authRpcServiceAsync.authAsync(authRpcAo);

        FutureListener authListener = new FutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                Exception authException = null;
                if (future.isSuccess()) {
                    AuthRpcVo authRpcVo = (AuthRpcVo) future.getValue();
                    logger.info("User rpc auth, authRpcVo={}", JSON.toJSONString(authRpcVo));

                    if (BaseRpcVo.isSuccessful(authRpcVo)) {
                        channel.setAuth(true);

                        Long uid = authRpcVo.getUid();
                        clientIdToUidMap.put(channel.getClientId(), uid);
                        uidToClientIdMap.put(uid, channel.getClientId());

                        //注册push node
                        pushNodeRedisDAO.set(uid, pushNode);
                    }
                } else {
                    logger.error("User rpc auth fail, authRpcAo={}", JSON.toJSONString(authRpcAo));
                    authException = future.getException();
                }

                router.channelAuth(channel, authException);
            }
        };

        future.addListener(authListener);
    }

    @Override
    public List<PushMessage> recoverPushMessage(MqttChannel channel) {
        List<PushMessage> messages = new ArrayList<>();
        Long uid = clientIdToUidMap.get(channel.getClientId());
        if (null == uid) {
            logger.error("Client has not been authed on this node, or logined on other node. clientId={}, remote={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return messages;
        }

        try {
            return pushMessageRedisDAO.getActivePushMessage(uid, PUSH_MESSAGE_TTL);
        } catch (Exception e) {
            logger.error("Get push message exception.clientId={}, romete={}, local={}",
                    channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
            return messages;
        }
    }

    @Override
    public void successPushMessage(PushMessage message) {
        logger.info("推送消息结束, message={}", JSON.toJSONString(message));

        PushMessage update = new PushMessage();
        update.setUid(message.getUid());
        update.setMessageId(message.getMessageId());
        update.setPushStatus(message.getPushStatus());
        update.setSuccessTime(message.getSuccessTime());
//        pushMessageRedisDAO.update(update);
//        pushMessageRedisDAO.deletePushQueue(message.getUid(), message.getMessageId());
        pushMessageRedisDAO.successPushMessage(update);

        //TODO:启动迁移任务
    }

    @Override
    public void prePushMessage(PushMessage message) {
        logger.info("推送消息预处理, message={}", JSON.toJSONString(message));

        PushMessage update = new PushMessage();
        update.setUid(message.getUid());
        update.setMessageId(message.getMessageId());
        update.setPushStatus(message.getPushStatus());
        update.setLastPushTime(message.getLastPushTime());
        logger.info("update={}", JSON.toJSONString(update));
//        pushMessageRedisDAO.update(update);
//        pushMessageRedisDAO.incrPushCount(update);
        pushMessageRedisDAO.preSuccessPushMessage(update);
    }

    @Override
    public void clusterChannelAuth(MqttChannel channel, MqttConnectMessage message, Router router) throws Exception {
        channel.setAuth(true);

        router.channelAuth(channel, null);
    }

    @Override
    public PushMessage pushMessage(MqttPublishMessage message) throws Exception {
        ByteBuf payload = message.payload();
        byte[] data = new byte[payload.readableBytes()];
        int mark = payload.readerIndex();
        payload.readBytes(data);
        payload.readerIndex(mark);

        PushMessage pushMessage = JSON.parseObject(new String(data, NetworkConstants.UTF8), PushMessage.class);

        String clientId = uidToClientIdMap.get(pushMessage.getUid());
        if (null == clientId) {
            logger.error("Client has not been authed on this node, or logined on other node. uid={}", pushMessage.getUid());
            return null;
        }

        pushMessage.setClientId(clientId);

        return pushMessage;
    }
}
