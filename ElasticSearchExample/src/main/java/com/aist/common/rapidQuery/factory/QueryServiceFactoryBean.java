package com.aist.common.rapidQuery.factory;

import com.aist.common.rapidQuery.service.impl.RapidQueryServiceImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cache.CacheManager;

import javax.sql.DataSource;

public class QueryServiceFactoryBean implements FactoryBean {
    
    private CacheManager cacheManager;
    private DataSource dataSource ;
    
    @Override
    public Object getObject() throws Exception {
        RapidQueryServiceImpl impl = new RapidQueryServiceImpl(cacheManager,dataSource);
        return impl;
    }

    @Override
    public Class getObjectType() {
        return RapidQueryServiceImpl.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
