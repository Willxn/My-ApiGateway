package com.willwei.gateway.session.handlers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.willwei.gateway.bind.IGenericReference;
import com.willwei.gateway.session.BaseHandler;
import com.willwei.gateway.session.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionServerHandler extends BaseHandler<FullHttpRequest> {
    //private final Logger logger = LoggerFactory.getLogger(SessionServerHandler.class);

    private final Configuration configuration;


    public SessionServerHandler(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, final Channel channel, FullHttpRequest request) {
        log.info("网关接收请求 uri：{} method：{}", request.uri(), request.method());

        // 返回信息控制：简单处理
        String methodName = request.uri().substring(1);
        if (methodName.equals("favicon.ico")) return;
        // 返回信息处理
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);


        // 服务泛化调用
        IGenericReference genericReference = configuration.getGenericReference(methodName);
        String result = genericReference.invoke("wxn");

//        ApplicationConfig applicationConfig = configuration.getApplicationConfig("api-gateway-test");
//        RegistryConfig registryConfig = configuration.getRegistryConfig("api-gateway-test");
//        ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig("com.weillwei.gateway.rpc.IActivityBooth");
//
//        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
//        bootstrap.application(applicationConfig)
//                .registry(registryConfig)
//                .reference(referenceConfig)
//                .start();
//
//        // 获取泛化服务实例
//        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
//        GenericService genericService = cache.get(referenceConfig);
//        Object result = genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"});


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

        System.out.println(response);

        channel.writeAndFlush(response);
    }
}
