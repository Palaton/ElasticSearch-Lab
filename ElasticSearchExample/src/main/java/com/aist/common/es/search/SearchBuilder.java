package com.aist.common.es.search;

import java.util.List;
import java.util.Map;

/**
 * Created by yangqun on 2016/10/24.
 */
public interface SearchBuilder<TQuery,TSearch extends Search<TQuery>> {

        SearchBuilder<TQuery,TSearch> setndex(String index);
        SearchBuilder<TQuery,TSearch> setType(String type);
        SearchBuilder<TQuery,TSearch> setQuery(TQuery query);
        SearchBuilder<TQuery,TSearch> setParameters(TQuery query, Map<String, Object> parameters);
        TSearch build();
}
