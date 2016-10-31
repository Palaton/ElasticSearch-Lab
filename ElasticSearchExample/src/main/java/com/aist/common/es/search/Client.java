package com.aist.common.es.search;

import com.aist.common.es.connect.EndPoint;

import java.util.List;

/**
 * Created by yangqun on 2016/10/31.
 */
public interface Client<TQuery> {

    <TResultContent,TResult extends Result<TResultContent>> TResult execute(EndPoint endPoint, Search<TQuery> search);

    <TResultContent,TResult extends Result<TResultContent>> TResult execute(Search<TQuery> search);
}
