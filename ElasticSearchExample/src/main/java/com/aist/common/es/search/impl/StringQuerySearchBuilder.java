package com.aist.common.es.search.impl;

import com.aist.common.es.search.AbstractSearchBuilder;
import com.aist.common.es.search.Search;

/**
 * Created by yangqun on 2016/10/31.
 */
public class StringQuerySearchBuilder extends AbstractSearchBuilder<String,StringQuerySearch>{

    public StringQuerySearchBuilder(){
    }

    @Override
    public StringQuerySearch build() {
        return new StringQuerySearch(index,type,query);
    }

}
