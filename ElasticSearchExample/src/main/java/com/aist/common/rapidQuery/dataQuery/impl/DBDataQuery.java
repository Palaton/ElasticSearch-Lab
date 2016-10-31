package com.aist.common.rapidQuery.dataQuery.impl;

import com.aist.common.rapidQuery.configParse.config.SearchScript;
import com.aist.common.rapidQuery.dataQuery.AbstractDataQuery;
import com.aist.common.rapidQuery.sqlBind.RapidSQLBuilder;
import com.aist.common.rapidQuery.sqlBind.VariableScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBDataQuery extends AbstractDataQuery {

    private static final Logger        LOGGER           = LoggerFactory
        .getLogger(DBDataQuery.class);

    private static final String        COUNT_CACHE_NAME = "rapidQueryCountCache";

    private NamedParameterJdbcTemplate jdbcTemplate;

    private CacheManager cacheManager;

    public DBDataQuery(NamedParameterJdbcTemplate jdbcTemplate, CacheManager cacheManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.cacheManager = cacheManager;
    }

    @Override
    public Page<Map<String, Object>> query(SearchScript script, Map<String, Object> paramMap) {
        if (script == null) {
            return null;
        }
        String baseSql = super.getScript(script.getNodeList(), paramMap);

        Integer count = queryCount(baseSql, script, paramMap);

        baseSql = RapidSQLBuilder.getPaginationSQL(baseSql,paramMap);
        List<Map<String, Object>> list = getResult(baseSql,script,paramMap);
        
        Pageable pageable = new PageRequest(Integer.valueOf(paramMap.get("pageNumber")+""), Integer.valueOf(paramMap.get("pageSize")+""));
        return new PageImpl<Map<String, Object>>(list, pageable, count);
    }

    private List<Map<String, Object>> getResult(String baseSql, SearchScript script,
                                                Map<String, Object> paramMap) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        String bindSql = VariableScript.bindVariable(paramSource,paramMap,baseSql);
        LOGGER.info("count sql：" + bindSql);
        LOGGER.info("count sql paramter：" + paramSource.getValues());
        List<Map<String, Object>> list = jdbcTemplate.queryForList(bindSql, paramSource);
        return list;
    }

    @Override
    public Page<Map<String, Object>> queryCount(SearchScript script, Map<String, Object> paramMap) {

        String baseSql = super.getScript(script.getNodeList(), paramMap);
        Integer count = queryCount(baseSql, script, paramMap);
        return new PageImpl<Map<String, Object>>(new ArrayList<Map<String, Object>>(), null, count);
    }

    private Integer queryCount(String baseSql, SearchScript script, Map<String, Object> paramMap) {
        
        Integer count = null;
        String countSql = RapidSQLBuilder.getCountSQL(baseSql, script.getPerformenceCountSql());

        if (script.getCacheCount()) {
            count = getQueryCache(countSql, paramMap);
        }

        if (count != null && count > 0) {
            return count;
        }

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        String bindCountSql = VariableScript.bindVariable(paramSource,paramMap,countSql);

        LOGGER.info("count sql：" + bindCountSql);
        LOGGER.info("count sql paramter：" + paramSource.getValues());

        count = jdbcTemplate.queryForObject(bindCountSql, paramSource, Integer.class);

        //更新缓存
        if (script.getCacheCount() && count > 0) {
            updateQueryCache(countSql, paramMap, count);
        }

        return count;
    }

    private Integer getQueryCache(String countSql, Map<String, Object> paramMap) {
        String key = countSql + "#" + String.valueOf(paramMap);
        Cache cache = cacheManager.getCache(COUNT_CACHE_NAME);
        if (cache != null) {
            ValueWrapper vw = cache.get(key);

            if (null == vw || null == vw.get()) {
                return null;
            }

            Object obj = vw.get();
            return Integer.valueOf(obj.toString());
        }
        return null;
    }

    private void updateQueryCache(String countSql, Map<String, Object> paramMap, Integer count) {
        String cacheName = countSql + "#" + String.valueOf(paramMap);
        Cache cache = cacheManager.getCache(COUNT_CACHE_NAME);
        cache.put(cacheName, count);
    }

    @Override
    public Page<Map<String, Object>> queryNoPagination(SearchScript script,
                                                       Map<String, Object> paramMap) {
        if (script == null) {
            return null;
        }
        String baseSql = super.getScript(script.getNodeList(), paramMap);
        List<Map<String, Object>> list = getResult(baseSql,script,paramMap);
        return new PageImpl<Map<String, Object>>(list, null, list.size());
    }

}
