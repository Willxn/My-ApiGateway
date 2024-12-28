package com.willwei.gateway.session.defaults;

import com.willwei.gateway.datasource.DataSource;
import com.willwei.gateway.datasource.DataSourceFactory;
import com.willwei.gateway.datasource.unpooled.UnpooledDataSourceFactory;
import com.willwei.gateway.executor.Executor;
import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.session.GatewaySession;
import com.willwei.gateway.session.GatewaySessionFactory;


public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        // 获取数据源连接信息，这里把 dubbo， http等抽象为一种链接资源
        DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        // 设置属性，UnpooledDataSourceFactory 默认是 调用 Dubbo
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();
        // 创建执行器
        Executor executor = configuration.newExecutor(dataSource.getConnection());
        // 创建会话：DefaultGatewaySession
        return new DefaultGatewaySession(configuration, uri, executor);
    }

}
