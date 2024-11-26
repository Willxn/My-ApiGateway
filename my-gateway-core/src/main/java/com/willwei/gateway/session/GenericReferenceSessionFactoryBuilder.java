package com.willwei.gateway.session;

import com.willwei.gateway.session.defaults.IGenericReferenceSessionFactoryImpl;

import java.util.concurrent.ExecutionException;

public class GenericReferenceSessionFactoryBuilder {
    public void build(Configuration configuration) throws ExecutionException, InterruptedException {
        IGenericReferenceSessionFactory IGenericReferenceSessionFactory = new IGenericReferenceSessionFactoryImpl();
        IGenericReferenceSessionFactory.build(configuration);
    }
}
