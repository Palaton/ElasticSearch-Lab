package com.aist.common.es.search.impl;

import com.aist.common.es.connect.EndPoint;
import com.aist.common.es.search.Search;

/**
 * Created by yangqun on 2016/10/31.
 */
public class PazoClient  extends AbstractHttpClient{

    public PazoClient(){
        searchAction="_coordinate_search";
    }

    @Override
    public <TResultContent, TResult extends com.aist.common.es.search.Result<TResultContent>> TResult execute(EndPoint endPoint, Search<String> search) {
        return null;
    }
}
