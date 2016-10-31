package com.aist.common.es.search;

import java.util.Map;

/**
 * Created by yangqun on 2016/10/24.
 */
public interface ParameterPreparer<TQuery> {
    TQuery prepare(TQuery query, Map<String, Object> args);
    TQuery prepare(TQuery query, String field, Object value);
}
