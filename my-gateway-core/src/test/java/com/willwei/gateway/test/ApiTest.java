package com.willwei.gateway.test;


import com.willwei.gateway.mapping.HttpCommandType;
import com.willwei.gateway.mapping.HttpStatement;
import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.socket.GatewaySocketServer;
import com.willwei.gateway.session.defaults.DefaultGatewaySessionFactory;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
public class ApiTest {

    /**
     * 测试：http://localhost:7397/wg/activity/sayHi
     */
    @Test
    public void test() throws ExecutionException, InterruptedException {

        // 1. 创建配置信息加载注册
        Configuration configuration = new Configuration();
        HttpStatement httpStatement = new HttpStatement(
                "api-gateway-test",
                "com.willwei.gateway.rpc.IActivityBooth",
                "sayHi",
                "/wg/activity/sayHi",
                HttpCommandType.GET
        );
        configuration.addMapper(httpStatement);

        // 2. 基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            log.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        log.info("netty server gateway start Done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);

//        SessionServer server = new SessionServer(configuration);
//        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
//        Channel channel = future.get();
//        if (null == channel) throw new RuntimeException("netty server start error channel is null");
//        while (!channel.isActive()) {
//            log.info("NettyServer启动服务 ...");
//            Thread.sleep(500);
//        }
//        log.info("NettyServer启动服务完成 {}", channel.localAddress());
//        Thread.sleep(Long.MAX_VALUE);
    }
}
