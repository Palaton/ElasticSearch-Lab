package com.aist.common.rapidQuery.dataQuery.impl;

import com.aist.common.rapidQuery.configParse.config.SearchScript;
import com.aist.common.rapidQuery.dataQuery.AbstractDataQuery;
import org.springframework.data.domain.Page;

import java.util.Map;

public class ESDataQuery extends AbstractDataQuery{

    @Override
    public Page<Map<String, Object>> query(SearchScript script, Map<String, Object> map) {
        
        return null;
    }

    @Override
    public Page<Map<String, Object>> queryCount(SearchScript script, Map<String, Object> map) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> queryNoPagination(SearchScript script,
                                                       Map<String, Object> map) {
        return null;
    }

}
