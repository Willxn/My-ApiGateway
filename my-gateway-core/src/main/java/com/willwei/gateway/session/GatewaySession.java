package com.willwei.gateway.session;

import com.willwei.gateway.bind.IGenericReference;

import java.util.Map;

public interface GatewaySession {
    Object get(String methodName, Map<String, Object> parameter);

    Object post(String methodName, Map<String, Object> args);

    IGenericReference getMapper(String uri);

    Configuration getConfiguration();


}
