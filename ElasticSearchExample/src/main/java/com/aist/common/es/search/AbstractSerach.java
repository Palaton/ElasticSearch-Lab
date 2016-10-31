package com.aist.common.es.search;

/**
 * Created by yangqun on 2016/10/31.
 */
public abstract class AbstractSerach<TQuery> implements Search<TQuery>{

    protected String index;
    protected String type;
    protected TQuery query;

    public AbstractSerach(String index, String type, TQuery query){
        this.index=index;
        this.type=type;
        this.query=query;
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public TQuery getQuery() {
        return query;
    }
}
