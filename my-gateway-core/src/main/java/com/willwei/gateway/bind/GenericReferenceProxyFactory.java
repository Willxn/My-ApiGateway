package com.willwei.gateway.bind;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericReferenceProxyFactory {

    private final GenericService genericService;

    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();
    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }

    public IGenericReference newInstance(String methodName) {
        return genericReferenceCache.computeIfAbsent(methodName, k->{
            GenericReferenceProxy genericReferenceProxy = new GenericReferenceProxy(genericService, methodName);
            // 定义接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(methodName, Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();

            // 创建代理
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);
            return (IGenericReference) enhancer.create();
        });
    }
}
