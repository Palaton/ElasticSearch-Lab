package com.aist.common.es.search;

import com.aist.common.es.connect.EndPoint;

import java.util.List;

/**
 * Created by yangqun on 2016/10/24.
 */
public abstract class AbstractClient<TQuery> implements Client<TQuery> {

    protected EndPoint endPoint;

    @Override
    public abstract <TResultContent, TResult extends com.aist.common.es.search.Result<TResultContent>> TResult execute(EndPoint endPoint, Search<TQuery> search);

    @Override
    public <TResultContent, TResult extends com.aist.common.es.search.Result<TResultContent>> TResult execute(Search<TQuery> search) {
        return execute(endPoint, search);
    }
}
