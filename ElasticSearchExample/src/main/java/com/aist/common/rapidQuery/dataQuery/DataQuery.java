package com.aist.common.rapidQuery.dataQuery;

import com.aist.common.rapidQuery.configParse.config.SearchScript;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DataQuery {
    /**
     * 数据查询接口
     * 
     * @param script
     * @param map
     * @return
     */
    Page<Map<String, Object>> query(SearchScript script, Map<String, Object> pram);
    
    /**
     * 查询计数
     * 
     * @param script
     * @param map
     * @return
     */
    Page<Map<String, Object>> queryCount(SearchScript script, Map<String, Object> map);
    
    /**
     * 查询计数
     * 
     * @param script
     * @param map
     * @return
     */
    Page<Map<String, Object>> queryNoPagination(SearchScript script, Map<String, Object> map);
}
