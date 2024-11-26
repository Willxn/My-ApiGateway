package com.willwei.gateway.session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

@Slf4j
public class SessionServer implements Callable<Channel> {

    //private final Logger logger = LoggerFactory.getLogger(SessionServer.class);

    private final EventLoopGroup boss = new NioEventLoopGroup(1);
    private final EventLoopGroup work = new NioEventLoopGroup();
    private Channel channel;
    private final Configuration configuration;

    public SessionServer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SessionChannelInitializer(configuration));

            channelFuture = b.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            log.error("socket server start error.", e);
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                log.info("socket server start done.");
            } else {
                log.error("socket server start error.");
            }
        }
        return channel;
    }

}