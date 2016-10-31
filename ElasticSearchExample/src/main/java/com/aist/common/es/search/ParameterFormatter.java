package com.aist.common.es.search;

/**
 * Created by yangqun on 2016/10/24.
 */
public interface ParameterFormatter<TResult> {
        TResult format(Object value);
}
