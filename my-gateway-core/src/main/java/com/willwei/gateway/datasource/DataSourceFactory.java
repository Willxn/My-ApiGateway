package com.willwei.gateway.datasource;

import com.willwei.gateway.session.Configuration;

public interface DataSourceFactory {
    void setProperties(Configuration configuration, String uri);

    DataSource getDataSource();
}
