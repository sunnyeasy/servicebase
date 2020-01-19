package com.easy.game.push.rpcimpl;

import com.easy.common.network.packet.PushMessage;
import com.easy.common.network.packet.RpcPushRequest;
import com.easy.common.rpcVo.BaseRpcVo;
import com.easy.push.rpcapi.GamePushRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePushRpcServiceImpl implements GamePushRpcService {
    private static final Logger logger = LoggerFactory.getLogger(GamePushRpcServiceImpl.class);

    @Override
    public BaseRpcVo push(RpcPushRequest request) {
        PushMessage message = new PushMessage();
        message.setMqttMessageId(0);
        message.setUid(request.getUid());
        message.setData(request.getData());

        return null;
    }
}
