package com.willwei.gateway.bind;

import com.willwei.gateway.mapping.HttpCommandType;
import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.GatewaySession;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperMethod {
    private String methodName;
    private final HttpCommandType command;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
    }

    public Object execute(GatewaySession session, Map<String, Object> args) {
        Object result = null;
        switch (command) {
            case GET:
                result = session.get(methodName, args);
                break;
            case POST:
                result = session.post(methodName, args);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command);
        }
        return result;
    }
}
