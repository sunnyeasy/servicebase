package com.easy.gateway.transport.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttTcpServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(MqttTcpServer.class);

    private volatile ServerState state = ServerState.UNINIT;

    private MqttConfig mqttConfig;
    private SslConfig sslConfig;
    private MqttRouter mqttRouter;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private MqttChannelHandler mqttChannelHandler;

    public MqttTcpServer(MqttConfig mqttConfig, SslConfig sslConfig, MqttRouter mqttRouter) {
        this.mqttConfig = mqttConfig;
        this.sslConfig = sslConfig;
        this.mqttRouter = mqttRouter;
    }

    @Override
    public boolean open() {
        if (state.isAliveState()) {
            logger.warn("MqttTcpServer already open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());
            return state.isAliveState();
        }

        logger.info("MqttTcpServer start open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());

        if (null == bossGroup) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst("idleStateHandler", new IdleStateHandler(mqttConfig.getReaderIdleTimeSeconds(), 0, 0));

                if (null != sslConfig) {
                    pipeline.addLast("ssl", SslHandlerFactory.buildSslHandler(sslConfig));
                }

                pipeline.addLast("decoder", new MqttDecoder(MqttConstants.MAX_CONTENT_LENGTH));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);

                mqttChannelHandler = new MqttChannelHandler(null, mqttRouter);
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

        logger.info("MqttTcpServer finish open: hostname={}, port={}", mqttConfig.getHostname(), mqttConfig.getPort());
        return state.isAliveState();
    }

    @Override
    public void close() {

    }

}
