package com.aist.common.rapidQuery.configParse.config;

import java.util.List;

/**
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author HuangSiwei
 * @create 2014年8月27日 上午9:21:11
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Query {

    private String           id;

    private SearchScript searchScript;

    private List<Column> columnList;
    
    /**
     * 权限
     */
    private String permission;
    
    /**
     * 返回 id 的值
     * 
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 id 的值
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public SearchScript getSearchScript() {
        return searchScript;
    }

    public void setSearchScript(SearchScript searchScript) {
        this.searchScript = searchScript;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
