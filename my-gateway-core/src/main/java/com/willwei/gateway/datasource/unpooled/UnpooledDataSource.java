package com.willwei.gateway.datasource.unpooled;

import com.willwei.gateway.datasource.Connection;
import com.willwei.gateway.datasource.DataSource;
import com.willwei.gateway.datasource.DataSourceType;
import com.willwei.gateway.datasource.connection.DubboConnection;
import com.willwei.gateway.mapping.HttpStatement;
import com.willwei.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

public class UnpooledDataSource implements DataSource {
    private Configuration configuration;
    private HttpStatement httpStatement;
    private DataSourceType dataSourceType;

    @Override
    public Connection getConnection() {
        switch (dataSourceType){
            case HTTP:
                // TODO:
                break;
            case Dubbo:
                // 配置信息
                String application = httpStatement.getApplication();
                String interfaceName = httpStatement.getInterfaceName();
                // 获取服务
                ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
                RegistryConfig registryConfig = configuration.getRegistryConfig(application);
                ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);
                return new DubboConnection(applicationConfig, registryConfig, reference);
            default:
                break;
        }
        throw new RuntimeException("DataSourceType：" + dataSourceType + "没有对应的数据源实现");
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }
    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }
}
