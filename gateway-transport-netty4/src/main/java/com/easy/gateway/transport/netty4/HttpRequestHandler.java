package com.easy.gateway.transport.netty4;

import com.easy.common.code.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;
    private GatewayListener gatewayListener;

    public HttpRequestHandler(ThreadPoolExecutor threadPoolExecutor, GatewayListener gatewayListener) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.gatewayListener = gatewayListener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //100 continue
        if (HttpUtil.is100ContinueExpected(req)) {
            //TODO:待确认
            ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
        }

        if (!req.method().equals(HttpMethod.POST)) {
            writeAndFlush(ctx, ResponseCode.METHOD_NOT_ALLOWED);
            return;
        }

        String uri = req.uri();
        if (StringUtils.isEmpty(uri)) {
            writeAndFlush(ctx, ResponseCode.NOT_FOUND);
            return;
        }

        String body = req.content().toString(CharsetUtil.UTF_8);

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FullHttpResponse response = null;
                try {
                    response = gatewayListener.route(ctx, uri, body);
                } catch (Exception e) {
                    logger.error("Route request exceptioned.", e);
                    response = AppResponseBuilder.build(ResponseCode.INTERNAL_SERVER_ERROR);
                }
                writeAndFlush(ctx, response);
            }
        });
    }

    private void writeAndFlush(ChannelHandlerContext ctx, ResponseCode code) {
        FullHttpResponse response = AppResponseBuilder.build(code);
        writeAndFlush(ctx, response);
    }

    private void writeAndFlush(ChannelHandlerContext ctx, FullHttpResponse response) {
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

