package com.easy.game.push.rpcimpl;

import com.alibaba.fastjson.JSON;
import com.easy.common.exception.BusinessException;
import com.easy.common.network.packet.push.RpcPushRequest;
import com.easy.common.rpcvo.BaseRpcVo;
import com.easy.game.push.GamePushResponseCode;
import com.easy.game.push.model.redis.PushNodeRedisDAO;
import com.easy.push.cluster.PushClusterClient;
import com.easy.push.registry.zookeeper.PushNode;
import com.easy.push.rpcapi.GamePushRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GamePushRpcServiceImpl implements GamePushRpcService {
    private static final Logger logger = LoggerFactory.getLogger(GamePushRpcServiceImpl.class);

    @Autowired
    private PushClusterClient clusterClient;

    @Autowired
    private PushNodeRedisDAO pushNodeRedisDAO;

    @Override
    public BaseRpcVo push(RpcPushRequest request) {
        BaseRpcVo vo = new BaseRpcVo();

        PushNode pushNode = null;
        try {
            pushNode = pushNodeRedisDAO.get(request.getUid());
        } catch (Exception e) {
            logger.error("Get push node exceptioned.uid={}", request.getUid(), e);
            vo.setCode(BusinessException.parseResponseCode(e));
            return vo;
        }
        if (null == pushNode) {
            logger.error("Not match push node.uid={}", request.getUid());
            vo.setCode(GamePushResponseCode.NOT_MATCH_PUSH_NODE);
            return vo;
        }

        try {
            clusterClient.push(pushNode, request);
        } catch (Exception e) {
            logger.error("Push message to terminnal client exceptioned.request={}", JSON.toJSONString(request), e);
            vo.setCode(BusinessException.parseResponseCode(e));
        }
        return vo;
    }
}
