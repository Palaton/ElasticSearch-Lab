package com.aist.common.es.search;

import com.aist.common.es.connect.EndPoint;

/**
 * Created by yangqun on 2016/10/31.
 */
public interface Search<TQuery> {
    String getIndex();
    String getType();
    TQuery getQuery();
}

