package com.aist.common.es.search.impl;

import com.aist.common.es.search.ParameterFormatter;
import com.aist.common.es.search.ParameterPlacePolder;
import com.aist.common.es.search.ParameterPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * Created by yangqun on 2016/10/24.
 */
public class StringQueryParameterPreparerImpl implements ParameterPreparer<String> {

    @Autowired
    protected ParameterFormatter<String> parameterFormatter;

    @Autowired
    protected ParameterPlacePolder parameterPlacePolder;

    @Override
    public String prepare(String query, Map<String, Object> args) {
        if(null==args||args.size()<1){
            return query;
        }
        for(String key : args.keySet()){
            query = prepare(query,key,args.get(key));
        }
        return query;
    }

    @Override
    public String prepare(String query, String field, Object value) {
        return query.replaceAll(String.format("(?i)%s",parameterPlacePolder.build(field)),parameterFormatter.format(value));
    }
}
