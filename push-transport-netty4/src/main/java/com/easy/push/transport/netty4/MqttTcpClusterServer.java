package com.easy.push.transport.netty4;

import com.easy.common.thread.StandardThreadExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttTcpClusterServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(MqttTcpClusterServer.class);

    private volatile ServerState state = ServerState.UNINIT;

    private MqttConfig mqttConfig;
    private MqttClusterRouter router;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private MqttChannelHandler mqttChannelHandler;
    private StandardThreadExecutor standardThreadExecutor;

    public MqttTcpClusterServer(MqttConfig mqttConfig, MqttClusterRouter router) {
        this.mqttConfig = mqttConfig;
        this.router = router;
    }

    @Override
    public boolean open() {
        if (state.isAliveState()) {
            logger.warn("MqttTcpClusterServer already open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());
            return state.isAliveState();
        }

        logger.info("MqttTcpClusterServer start open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());

        if (null == bossGroup) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        standardThreadExecutor = (standardThreadExecutor != null && !standardThreadExecutor.isShutdown()) ? standardThreadExecutor
                : new StandardThreadExecutor(mqttConfig.getMinWorkerThread(), mqttConfig.getMaxWorkerThread(), mqttConfig.getWorkerQueueSize(), new DefaultThreadFactory("mqttTcpClusterServer-" + mqttConfig.getPort(), true));

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst("idleStateHandler", new IdleStateHandler(mqttConfig.getReaderIdleTimeSeconds(), 0, 0));

                pipeline.addLast("decoder", new MqttDecoder(MqttConstants.MAX_CONTENT_LENGTH));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);

                mqttChannelHandler = new MqttChannelHandler(standardThreadExecutor, router);
                pipeline.addLast("handler", mqttChannelHandler);
            }
        });

        serverBootstrap.option(ChannelOption.SO_BACKLOG, MqttConstants.SO_BACKLOG);
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childOption(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));

        ChannelFuture channelFuture = serverBootstrap.bind(mqttConfig.getHostname(), mqttConfig.getPort());
        channelFuture.syncUninterruptibly();
        state = ServerState.ALIVE;

        logger.info("MqttTcpClusterServer finish open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());
        return state.isAliveState();
    }

    @Override
    public void close() {

    }

}
