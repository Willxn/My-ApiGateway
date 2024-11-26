package com.willwei.gateway.session;

import com.willwei.gateway.session.handlers.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Configuration configuration;

    public SessionChannelInitializer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline line = channel.pipeline();
        line.addLast(new HttpRequestDecoder());
        line.addLast(new HttpResponseEncoder());
        line.addLast(new HttpObjectAggregator(1024 * 1024));
        line.addLast(new SessionServerHandler(configuration));
    }
}
