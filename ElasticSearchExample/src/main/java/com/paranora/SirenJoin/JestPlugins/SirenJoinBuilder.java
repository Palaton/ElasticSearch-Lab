package com.paranora.SirenJoin.JestPlugins;

import io.searchbox.core.Search;

/**
 * Created by yangqun on 2016/09/19.
 */
public class SirenJoinBuilder extends Search.Builder {

    public SirenJoinBuilder(String query) {
       super(query);
    }

    @Override
    public Search build() {
        return new SirenJoinSearch(this);
    }
}
