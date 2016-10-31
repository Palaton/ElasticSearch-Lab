package com.aist.common.es.search;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by yangqun on 2016/10/24.
 */
public interface Validator<TQuery> {
    boolean Validate(TQuery query);
}
