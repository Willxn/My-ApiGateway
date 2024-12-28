package com.willwei.gateway.session.defaults;

import com.willwei.gateway.bind.IGenericReference;
import com.willwei.gateway.datasource.Connection;
import com.willwei.gateway.datasource.DataSource;
import com.willwei.gateway.executor.Executor;
import com.willwei.gateway.mapping.HttpStatement;
import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.GatewaySession;
import com.willwei.gateway.util.SimpleTypeUtil;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

/**
 * 默认网关会话实现类
 *
 */

public class DefaultGatewaySession implements GatewaySession {
    private final Configuration configuration;
    private final String uri;
    private final Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri, Executor executor) {
        this.configuration = configuration;
        this.uri = uri;
        this.executor = executor;
    }


    @Override
    public Object get(String methodName, Map<String, Object> paramMap) {
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        try {
            return executor.exec(httpStatement, paramMap);
        } catch (Exception e) {
            throw new RuntimeException("Error exec get. Cause: " + e);
        }


    }

    @Override
    public IGenericReference getMapper(String uri) {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public Object post(String methodName, Map<String, Object> args) {
        return get(methodName, args);
    }
}
