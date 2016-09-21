package com.paranora.SirenJoin.JestPlugins;

import io.searchbox.core.Search;

/**
 * Created by yangqun on 2016/09/19.
 */
public class SirenJoinSearch extends Search {

    public SirenJoinSearch(Builder builder){
        super(builder);
    }

    @Override
    protected String buildURI(){
        return super.buildURI().replace("_search","_coordinate_search");
    }
}
