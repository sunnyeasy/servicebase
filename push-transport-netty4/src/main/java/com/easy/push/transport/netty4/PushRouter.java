package com.easy.push.transport.netty4;

import com.easy.common.network.packet.PushMessage;

public interface PushRouter {
    void pushMessage(PushMessage pushMessage);
}
