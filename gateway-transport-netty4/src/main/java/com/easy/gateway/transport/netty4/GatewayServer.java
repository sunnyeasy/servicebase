package com.easy.gateway.transport.netty4;

import com.easy.common.thread.StandardThreadExecutor;
import com.easy.common.transport.Server;
import com.easy.common.transport.ServerState;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewayServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);

    private volatile ServerState state = ServerState.UNINIT;

    private GatewayConfig gatewayConfig;
    private GatewayListener gatewayListener;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private StandardThreadExecutor standardThreadExecutor;

    public GatewayServer(GatewayConfig gatewayConfig, GatewayListener gatewayListener) {
        this.gatewayConfig = gatewayConfig;
        this.gatewayListener = gatewayListener;
    }

    @Override
    public boolean open() {
        if (state.isAliveState()) {
            logger.warn("MqttTcpServer already open: hostname={}, port={}", gatewayConfig.getHostname(), gatewayConfig.getPort());
            return state.isAliveState();
        }

        if (null == bossGroup) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        standardThreadExecutor = (standardThreadExecutor != null && !standardThreadExecutor.isShutdown()) ? standardThreadExecutor
                : new StandardThreadExecutor(gatewayConfig.getMinWorkerThread(), gatewayConfig.getMaxWorkerThread(), gatewayConfig.getWorkerQueueSize(), new DefaultThreadFactory("gatewayServer-" + gatewayConfig.getPort(), true));

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst("idleStateHandler", new IdleStateHandler(gatewayConfig.getReaderIdleTimeSeconds(), 0, 0));

                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast("httpAggregator", new HttpObjectAggregator(GatewayConstants.MAX_CONTENT_LENGTH));
                pipeline.addLast(new HttpRequestHandler(standardThreadExecutor, gatewayListener));
            }
        });

        serverBootstrap.option(ChannelOption.SO_BACKLOG, GatewayConstants.SO_BACKLOG);
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childOption(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false));

        ChannelFuture channelFuture = serverBootstrap.bind(gatewayConfig.getHostname(), gatewayConfig.getPort());
        channelFuture.syncUninterruptibly();
        state = ServerState.ALIVE;

        logger.info("GatewayServer open successfully: hostname={}, port={}", gatewayConfig.getHostname(), gatewayConfig.getPort());
        return state.isAliveState();
    }

    @Override
    public void close() {

    }

}
