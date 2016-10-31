package com.aist.common.rapidQuery.configParse.config;

import com.aist.common.rapidQuery.scriptParse.BaseNode;

import java.util.List;

public class SearchScript {
    /**
     * es 查询 index
     */
    private String index ;
    
    /**
     * es 查询类型
     */
    private SearchTpye type = SearchTpye.DB;
    
    /**
     * 是否优化sql
     */
    private Boolean performenceCountSql = false;
    
    /**
     * 缓存count
     */
    private Boolean         cacheCount = false;
    
    /**
     * 缓存结果
     */
    private Boolean         cacheResult = false;
    
    private List<BaseNode> nodeList;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public SearchTpye getType() {
        return type;
    }

    public void setType(SearchTpye type) {
        this.type = type;
    }

    public List<BaseNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<BaseNode> nodeList) {
        this.nodeList = nodeList;
    }

    public Boolean getPerformenceCountSql() {
        return performenceCountSql;
    }

    public void setPerformenceCountSql(Boolean performenceCountSql) {
        this.performenceCountSql = performenceCountSql;
    }

    public Boolean getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(Boolean cacheCount) {
        this.cacheCount = cacheCount;
    }

    public Boolean getCacheResult() {
        return cacheResult;
    }

    public void setCacheResult(Boolean cacheResult) {
        this.cacheResult = cacheResult;
    }
}
