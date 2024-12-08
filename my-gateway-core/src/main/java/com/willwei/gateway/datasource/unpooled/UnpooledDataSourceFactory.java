package com.willwei.gateway.datasource.unpooled;

import com.willwei.gateway.datasource.DataSource;
import com.willwei.gateway.datasource.DataSourceFactory;
import com.willwei.gateway.datasource.DataSourceType;
import com.willwei.gateway.session.Configuration;

public class UnpooledDataSourceFactory implements DataSourceFactory {

    private UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setHttpStatement(configuration.getHttpStatement(uri));
        this.dataSource.setDataSourceType(DataSourceType.Dubbo);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
