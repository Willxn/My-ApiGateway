package com.willwei.gateway.datasource;

public interface Connection {
    Object execute(String method, String[] parameterTypes, Object[] args);

}
