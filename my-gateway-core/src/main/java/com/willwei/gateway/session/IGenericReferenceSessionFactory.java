package com.willwei.gateway.session;

import java.util.concurrent.ExecutionException;

public interface IGenericReferenceSessionFactory {

    void build(Configuration configuration) throws InterruptedException, ExecutionException;
}
