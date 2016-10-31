package com.aist.common.es.search;

import java.util.Map;

/**
 * Created by yangqun on 2016/10/31.
 */
public interface QuerySerializer<TQuery> {
    String serialize(TQuery query, Map<String, Object> parameters);
}
