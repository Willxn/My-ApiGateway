package com.willwei.gateway.socket.agreement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * HTTP 请求处理类，
 * 需要处理的请求类型
 *
 *
 */

@Slf4j
public class RequestParser {
    private final FullHttpRequest request;
    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    public Map<String, Object> parse() {
        final Map<String, Object> paramMap = new HashMap<>();
        // 解析 GET 请求
        if (HttpMethod.GET == request.method()) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> paramMap.put(key, value.get(0))); // 仅取第一个值
            return paramMap;
        } else if (HttpMethod.POST == request.method()) {
            log.info("=========================进入post请求");
            String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);

            // 解析 multipart/form-data
            if (contentType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())) {
                // 解析 multipart/form-data
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                decoder.offer(request);
                decoder.getBodyHttpDatas().forEach(data -> {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        Attribute attribute = (Attribute) data;
                        try {
                            paramMap.put(attribute.getName(), attribute.getValue());
                        } catch (IOException e) {
                            log.error("解析表单字段失败", e);
                        }
                    }
                    // 可以扩展解析文件上传逻辑
                });

            // 解析 application/json
            } else if (contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString())) {
                log.info("=======================进入json");
                String jsonContent = request.content().toString(CharsetUtil.UTF_8);
                return JSON.parseObject(jsonContent);

            } else{
                throw new RuntimeException("未实现的协议类型 Content-Type: " + contentType);
            }
        }
        throw new RuntimeException("未实现的请求类型 HttpMethod: "+ request.method());
    }
}