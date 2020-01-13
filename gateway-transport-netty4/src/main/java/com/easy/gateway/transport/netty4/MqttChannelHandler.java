package com.easy.gateway.transport.netty4;

import com.easy.common.exception.BusinessException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

public class MqttChannelHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(MqttChannelHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;
    private MqttRouter mqttRouter;

    public MqttChannelHandler(ThreadPoolExecutor threadPoolExecutor, MqttRouter mqttRouter) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.mqttRouter = mqttRouter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Accept connection, remote={}, local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Disconnect, remote={}, local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof MqttMessage)) {
            logger.error("Received message type is not support: class={}", msg.getClass().getName());
            throw new BusinessException(MqttResponseCode.MESSAGE_TYPE_NOT_SUPPORT);
        }

        MqttMessage mqttMessage = (MqttMessage) msg;
        if (mqttMessage.decoderResult().isFailure()) {
            logger.error("Decode mqtt message error. remote={}, local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
            exceptionCaught(ctx, mqttMessage.decoderResult().cause());
            return;
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MqttChannel channel = MqttChannelFactory.channel(ctx.channel());
                try {
                    mqttRouter.processMessage(channel, mqttMessage);
                } catch (Exception e) {
                    logger.error("Process message exception, clientId={}, remote={}, local={}", channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
                    ctx.fireExceptionCaught(e);
                }
            }
        });
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }
}
