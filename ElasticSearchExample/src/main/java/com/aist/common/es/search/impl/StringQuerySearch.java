package com.aist.common.es.search.impl;

import com.aist.common.es.search.AbstractSerach;
import com.aist.common.es.search.Search;

/**
 * Created by yangqun on 2016/10/31.
 */
public class StringQuerySearch extends AbstractSerach<String> {
    public StringQuerySearch(String index,String type,String query){
        super(index,type,query);
    }
}
