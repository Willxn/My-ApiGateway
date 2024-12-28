package com.willwei.gateway.socket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.willwei.gateway.bind.IGenericReference;
import com.willwei.gateway.session.GatewaySession;
import com.willwei.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.willwei.gateway.socket.BaseHandler;
import com.willwei.gateway.socket.agreement.RequestParser;
import com.willwei.gateway.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {
    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        log.info("网关接收请求 uri：{} method：{}", request.uri(), request.method());

        // 1.解析请求参数
        RequestParser parser = new RequestParser(request);
        String uri = parser.getUri();
        if(null == uri) return;
        Map<String, Object> requestObj = new RequestParser(request).parse();

        // 2.调用会话服务
        // 从网关会话工厂中创建一个网关会话对象
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        //拿到 IGenericReference 的代理对象
        IGenericReference reference = gatewaySession.getMapper(uri);
        //调用代理对象的invoke方法，等同于调用 MapperProxy 的 intercept 方法
        log.info("=================" + requestObj.toString());
        Object result = reference.invoke(requestObj);

        // 3. 封装返回结果
        DefaultFullHttpResponse response = new ResponseParser().parse(result);
        channel.writeAndFlush(response);

    }
}
