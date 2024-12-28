package com.willwei.gateway.executor;

import com.willwei.gateway.executor.result.GatewayResult;
import com.willwei.gateway.mapping.HttpStatement;

import java.util.Map;

public interface Executor {
    GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;
}
