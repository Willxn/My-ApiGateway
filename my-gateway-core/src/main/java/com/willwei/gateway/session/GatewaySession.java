package com.willwei.gateway.session;

import com.willwei.gateway.bind.IGenericReference;

public interface GatewaySession {
    Object get(String uri, Object parameter);

    IGenericReference getMapper(String uri);

    Configuration getConfiguration();
}
