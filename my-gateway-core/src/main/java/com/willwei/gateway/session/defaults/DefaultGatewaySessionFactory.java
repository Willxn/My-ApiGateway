package com.willwei.gateway.session.defaults;

import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.GatewaySession;
import com.willwei.gateway.session.GatewaySessionFactory;

public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession() {
        return new DefaultGatewaySession(configuration);
    }

}
