package com.aist.common.es.search.impl;

import com.aist.common.es.search.ParameterFormatter;
import org.apache.poi.xslf.model.geom.Guide;
import org.hibernate.id.GUIDGenerator;

import java.util.UUID;

/**
 * Created by yangqun on 2016/10/24.
 */
public class StringParameterFormatter implements ParameterFormatter<String> {

    @Override
    public String format(Object value) {
        String result="";
        if(value instanceof String || value instanceof UUID){
            result=String.format("\"%s\"",value);
        } else{
            result=value.toString();
        }
        return result;
    }
}
