package com.aist.common.es.search.impl;

import com.aist.common.es.connect.EndPoint;
import com.aist.common.es.search.AbstractClient;
import com.aist.common.es.search.Search;

/**
 * Created by yangqun on 2016/10/31.
 */
public abstract class AbstractHttpClient extends AbstractClient<String> {

    protected String searchAction="_search";

    protected String getSearchAddress(EndPoint endPoint,Search<String> search){
        return String.format("%s:%s/%s/%s/%s", endPoint.getAddress(),endPoint.getPort(),search.getIndex(),search.getType(),searchAction);
    }
}
