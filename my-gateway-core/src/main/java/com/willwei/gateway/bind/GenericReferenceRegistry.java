package com.willwei.gateway.bind;

import com.willwei.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

public class GenericReferenceRegistry {

    private final Configuration configuration;
    private final Map<String, GenericReferenceProxyFactory> knownGenericReferenceMap = new HashMap<>();

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getGenericReference(String methodName){
        GenericReferenceProxyFactory genericReferenceProxyFactory = knownGenericReferenceMap.get(methodName);
        if(genericReferenceProxyFactory == null){
            throw new RuntimeException("Method: " + methodName + "is not known to GenericReferenceRegistry");
        }
        return genericReferenceProxyFactory.newInstance(methodName);
    }

    public void addGenericReference(String application, String interfaceName, String methodName) {

        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig(interfaceName);

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        // 获取泛化服务实例
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(referenceConfig);

       knownGenericReferenceMap.put(methodName, new GenericReferenceProxyFactory(genericService));

    }
}
