package com.willwei.gateway.test;


import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.GenericReferenceSessionFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.ExecutionException;


@Slf4j
public class ApiTest {
    //private final Logger logger =  LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Configuration configuration = new Configuration();

        // 给 configuration 添加泛化调用的接口方法信息
        configuration.addGenericReference("api-gateway-test", "com.willwei.gateway.rpc.IActivityBooth", "sayHi");

        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
        builder.build(configuration);

        log.info("test方法服务启动完成");

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
