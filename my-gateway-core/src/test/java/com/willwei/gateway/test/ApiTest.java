package com.willwei.gateway.test;


import com.willwei.gateway.session.SessionServer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class ApiTest {
    //private final Logger logger =  LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        SessionServer server = new SessionServer();

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            log.info("NettyServer启动服务 ...");
            Thread.sleep(500);
        }
        log.info("NettyServer启动服务完成 {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }
}
