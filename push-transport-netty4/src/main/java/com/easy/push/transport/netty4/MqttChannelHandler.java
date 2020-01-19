package com.easy.push.transport.netty4;

import com.easy.common.exception.BusinessException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class MqttChannelHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(MqttChannelHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;
    private Router router;

    public MqttChannelHandler(ThreadPoolExecutor threadPoolExecutor, Router router) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.router = router;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Accept connection, remote={}, local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Disconnect, remote={}, local={}", ctx.channel().remoteAddress(), ctx.channel().localAddress());

        MqttChannel channel = MqttChannelFactory.getChannel(ctx.channel());
        if (null == channel) {
            return;
        }

        router.processDisConnect(channel);
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

        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    MqttChannel channel = MqttChannelFactory.makeChannel(ctx.channel());
                    try {
                        router.processMessage(channel, mqttMessage);
                    } catch (Exception e) {
                        logger.error("Process message exception, clientId={}, remote={}, local={}",
                                channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress(), e);
                        ctx.fireExceptionCaught(e);
                    }
                }
            });
        } catch (RejectedExecutionException rejectException) {
            logger.error("Process thread pool is full, reject, active={}, poolSize={}, corePoolSize={}, maxPoolSize={}, taskCount={}",
                    threadPoolExecutor.getActiveCount(), threadPoolExecutor.getPoolSize(), threadPoolExecutor.getCorePoolSize(),
                    threadPoolExecutor.getMaximumPoolSize(), threadPoolExecutor.getTaskCount());
            rejectMessage(ctx.channel(), mqttMessage);
        }
    }

    private void rejectMessage(Channel channel, MqttMessage message) {
        //TODO:
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        MqttChannel channel = MqttChannelFactory.makeChannel(ctx.channel());
        if (null == channel) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent stateEvent = (IdleStateEvent) evt;
                if (stateEvent.state() == IdleState.READER_IDLE) {
                    logger.info("MqttChannel has not been make，Reader channel is idle，close channel, evt={}, remote={}, local={}",
                            evt.getClass(), ctx.channel().remoteAddress(), ctx.channel().localAddress());
                    ctx.channel().close();
                }
            } else {
                logger.error("Event error, MqttChannel has not been make, evt={}, remote={}, local={}",
                        evt.getClass(), ctx.channel().remoteAddress(), ctx.channel().localAddress());
            }
            return;
        }

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state() == IdleState.READER_IDLE) {
                logger.info("Reader channel is idle，close channel, evt={}, clientId={}, remote={}, local={}",
                        evt.getClass(), channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
                ctx.channel().close();
            }
        } else {
            logger.error("Event error, evt={}, clientId={}, remote={}, local={}",
                    evt.getClass(), channel.getClientId(), channel.getChannel().remoteAddress(), channel.getChannel().localAddress());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("MqttChannelHandler exceptionCaught: remote={} local={} event={}",
                ctx.channel().remoteAddress(), ctx.channel().localAddress(), cause.getMessage(), cause);
        ctx.channel().close();
    }
}
