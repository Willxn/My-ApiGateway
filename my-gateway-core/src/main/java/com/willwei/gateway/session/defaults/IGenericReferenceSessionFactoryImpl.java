package com.willwei.gateway.session.defaults;

import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.IGenericReferenceSessionFactory;
import com.willwei.gateway.session.SessionServer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class IGenericReferenceSessionFactoryImpl implements IGenericReferenceSessionFactory {

    @Override
    public void build(Configuration configuration) throws InterruptedException, ExecutionException {
        SessionServer server = new SessionServer(configuration);
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();
        if (null == channel) throw new RuntimeException("netty server start error channel is null");
        while (!channel.isActive()) {
            log.info("NettyServer启动服务 ...");
            Thread.sleep(500);
        }
        log.info("NettyServer启动服务完成 {}", channel.localAddress());
    }
}
