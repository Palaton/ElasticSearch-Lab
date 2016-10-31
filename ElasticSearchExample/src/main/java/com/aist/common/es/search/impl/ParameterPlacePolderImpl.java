package com.aist.common.es.search.impl;

import com.aist.common.es.search.ParameterPlacePolder;
import org.springframework.beans.factory.annotation.Value;


/**
 * Created by yangqun on 2016/10/24.
 */
public class ParameterPlacePolderImpl implements ParameterPlacePolder {

    @Value("@")
    protected String prefix;

    @Value("")
    protected String suffix;

    @Override
    public String build(String field) {
        return String.format("%s%s%s",prefix,field,suffix);
    }
}
