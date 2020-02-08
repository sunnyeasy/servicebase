package com.easy.push.transport.netty4;

import com.easy.common.network.packet.push.PushMessage;

public interface PushRouter {
    void pushMessage(PushMessage pushMessage);
}
