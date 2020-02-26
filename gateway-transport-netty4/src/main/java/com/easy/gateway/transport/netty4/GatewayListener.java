package com.easy.gateway.transport.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

public interface GatewayListener {
    FullHttpResponse route(ChannelHandlerContext ctx, String uri, String body);
}
