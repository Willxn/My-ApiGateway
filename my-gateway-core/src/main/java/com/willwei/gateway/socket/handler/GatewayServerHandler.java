package com.willwei.gateway.socket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.willwei.gateway.bind.IGenericReference;
import com.willwei.gateway.session.GatewaySession;
import com.willwei.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.willwei.gateway.socket.BaseHandler;
import com.willwei.gateway.socket.agreement.RequestParser;
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

        // 解析请求参数
        Map<String, Object> requestObj = new RequestParser(request).parse();

        // 返回信息控制：简单处理
        //http://localhost:7397/wg/activity/sayHi?str=10001
        // 返回信息控制：简单处理
        String uri = request.uri();       //   uri: /wg/activity/sayHi?str=10001
        int index = uri.indexOf("?");     //   uri: /wg/activity/sayHi
        uri = index > 0 ?  uri.substring(0, index) : uri;
        if (uri.equals("/favicon.ico")) return;

        // 从网关会话工厂中创建一个网关会话对象
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        //拿到 IGenericReference 的代理对象
        IGenericReference reference = gatewaySession.getMapper(uri);
        //调用代理对象的invoke方法，等同于调用 MapperProxy 的 intercept 方法
        log.info("=================" + requestObj.toString());
        String result = reference.invoke(requestObj) + " " + System.currentTimeMillis();

        // 返回信息处理
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // 设置回写数据
        response.content().writeBytes(JSON.toJSONBytes(result, SerializerFeature.PrettyFormat));

        // 头部信息设置
        HttpHeaders heads = response.headers();
        // 返回内容类型
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 响应体的长度
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 配置持久连接
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // 配置跨域访问
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        channel.writeAndFlush(response);
    }
}
