package com.easy.game.push.rpcimpl;

import com.alibaba.fastjson.JSON;
import com.easy.common.errorcode.ResponseCode;
import com.easy.common.exception.BusinessException;
import com.easy.common.id.IdUtils;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.common.transport.packet.push.PushMessage;
import com.easy.common.transport.packet.push.RpcPushRequest;
import com.easy.game.push.ConsistentHash;
import com.easy.game.push.GamePushResponseCode;
import com.easy.game.push.model.redis.dao.PushMessageRedisDAO;
import com.easy.game.push.model.redis.dao.PushNodeRedisDAO;
import com.easy.push.cluster.PushClusterClient;
import com.easy.push.registry.zookeeper.ClusterZookeeperRegistry;
import com.easy.push.registry.zookeeper.PushNode;
import com.easy.push.rpcao.PushServerRpcAo;
import com.easy.push.rpcapi.GamePushRpcService;
import com.easy.push.rpcvo.PushServerRpcVo;
import com.easy.push.transport.netty4.PushStatus;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Map;

@MotanService
public class GamePushRpcServiceImpl implements GamePushRpcService {
    private static final Logger logger = LoggerFactory.getLogger(GamePushRpcServiceImpl.class);
    private static final String GAME_PUSH_TOPIC = "/game/server/to/client/";

    @Autowired
    private PushClusterClient clusterClient;

    @Autowired
    private PushNodeRedisDAO pushNodeRedisDAO;
    @Autowired
    private PushMessageRedisDAO pushMessageRedisDAO;
    @Autowired
    private ClusterZookeeperRegistry clusterZookeeperRegistry;

    @Override
    public BaseRpcVo push(RpcPushRequest request) {
        BaseRpcVo vo = new BaseRpcVo();

        PushMessage pushMessage = new PushMessage();
        pushMessage.setMessageId(IdUtils.getInstance().createPushMessageId());
        pushMessage.setClientId(null);
        String topic = GAME_PUSH_TOPIC + request.getUid();
        pushMessage.setTopic(topic);
        //pushMessage.setMqttMessageId(-1);
        pushMessage.setUid(request.getUid());
        pushMessage.setData(request.getData());
        pushMessage.setPushStatus(PushStatus.waiting.getCode());
        pushMessage.setPushCount(0);
        pushMessage.setCreateTime(System.currentTimeMillis());
        pushMessage.setLastPushTime(0);
        pushMessage.setSuccessTime(0);
        pushMessage.setTimeoutTime(0);

        try {
            pushMessageRedisDAO.save(pushMessage);
        } catch (Exception e) {
            logger.error("Save push message exceptioned.uid={}", request.getUid(), e);
            vo.setCode(GamePushResponseCode.SAVE_PUSH_MESSAGE_ERROR);
            return vo;
        }

        PushNode pushNode = null;
        try {
            pushNode = pushNodeRedisDAO.get(request.getUid());
        } catch (Exception e) {
            logger.error("Get push node exceptioned.uid={}", request.getUid(), e);
            vo.setCode(GamePushResponseCode.NOT_MATCH_PUSH_NODE);
            return vo;
        }
        if (null == pushNode) {
            logger.error("Not match push node.uid={}", request.getUid());
            vo.setCode(GamePushResponseCode.NOT_MATCH_PUSH_NODE);
            return vo;
        }

        try {
            clusterClient.push(pushNode, pushMessage);
        } catch (Exception e) {
            logger.error("Push message to terminnal client exceptioned.request={}", JSON.toJSONString(request), e);
            vo.setCode(BusinessException.parseResponseCode(e));
        }
        return vo;
    }

    @Override
    public PushServerRpcVo queryPushServer(PushServerRpcAo rpcAo) {
        PushServerRpcVo rpcVo;
        try {
            rpcVo = doQueryPushServer(rpcAo);
        } catch (Exception e) {
            logger.error("Query push server node exception, rpcAo={}", JSON.toJSONString(rpcAo), e);

            rpcVo = new PushServerRpcVo();
            rpcVo.setCode(BusinessException.parseResponseCode(e));
        }
        return rpcVo;
    }

    private PushServerRpcVo doQueryPushServer(PushServerRpcAo rpcAo) {
        Map<String, PushNode> nodeMap = clusterZookeeperRegistry.getPushNodeMap();
        if (CollectionUtils.isEmpty(nodeMap)) {
            throw new BusinessException(ResponseCode.NULL_DATA);
        }

        ConsistentHash<PushNode> hash = new ConsistentHash<>(new ArrayList<>(nodeMap.values()));
        PushNode node = hash.select(rpcAo.getUid());

        if (null == node) {
            throw new BusinessException(ResponseCode.NULL_DATA);
        }

        PushServerRpcVo rpcVo = new PushServerRpcVo();
        rpcVo.setHostname(node.getHostname());
        rpcVo.setPort(node.getPort());
        return rpcVo;

    }
}
