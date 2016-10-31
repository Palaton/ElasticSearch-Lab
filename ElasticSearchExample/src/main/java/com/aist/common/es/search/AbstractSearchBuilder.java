package com.aist.common.es.search;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by yangqun on 2016/10/31.
 */
public abstract class AbstractSearchBuilder<TQuery, TSearch extends Search<TQuery>> implements SearchBuilder<TQuery, TSearch> {

    protected String index;
    protected String type;
    protected TQuery query;

    @Autowired
    protected ParameterPreparer<TQuery> parameterPreparer;

    public AbstractSearchBuilder() {

    }


    @Override
    public SearchBuilder<TQuery, TSearch> setndex(String index) {
        this.index = index;
        return this;
    }

    @Override
    public SearchBuilder<TQuery, TSearch> setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public SearchBuilder<TQuery, TSearch> setQuery(TQuery tQuery) {
        this.query=tQuery;
        return this;
    }

    @Override
    public SearchBuilder<TQuery, TSearch> setParameters(TQuery tQuery, Map<String, Object> parameters) {
        this.query=parameterPreparer.prepare(tQuery, parameters);
        return this;
    }

    @Override
    public abstract TSearch build();
}
