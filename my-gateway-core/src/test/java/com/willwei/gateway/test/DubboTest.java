package com.willwei.gateway.test;


import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;


public class DubboTest {

    //public final GenericService genericService;

    @Test
    public void testDubbo(){
        // 创建应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("api-gateway-test");

        // 创建注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181"); // 替换为你的 Zookeeper 地址


        // 创建引用配置
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setInterface("com.willwei.gateway.rpc.IActivityBooth"); // 替换为你的服务接口名称
        referenceConfig.setGeneric("true"); // 启用泛化调用


        // 获取泛化服务
        GenericService genericService = referenceConfig.get();

        // 调用 sayHi 方法
        Object result = genericService.$invoke(
                "sayHi", // 方法名
                new String[]{"java.lang.String"}, // 方法参数类型
                new Object[]{"World"} // 方法参数值
        );

        // 输出结果
        System.out.println("Response from sayHi: " + result);
    }
}
