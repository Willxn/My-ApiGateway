package com.willwei.gateway.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.Arrays;

public class GenericReferenceProxy implements MethodInterceptor {

    private final GenericService genericService;

    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String[] parameters = Arrays.stream(method.getParameterTypes())
                .map(Class::getName)
                .toArray(String[]::new);
        // 举例：genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"})
        return genericService.$invoke(methodName, parameters, args);
    }
}
