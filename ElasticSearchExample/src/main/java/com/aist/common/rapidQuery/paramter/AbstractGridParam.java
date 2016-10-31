package com.aist.common.rapidQuery.paramter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author HuangSiwei
 * @create 2014年9月19日 下午5:01:57
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

public abstract class AbstractGridParam {
    private String              queryId;
    /** 参数列表 */
    private Map<String, Object> paramMap       = new HashMap<String, Object>();

    /** 导出显示列  */
    private List<String>        displayColumns = new ArrayList<String>();

    public void addDisplayColumns(String col) {
        if (StringUtils.isBlank(col)) {
            return;
        }
        displayColumns.add(col);
    }

    public void addAllDisplayColumns(List<String> col) {
        if (null == col || col.isEmpty()) {
            return;
        }
        displayColumns.addAll(col);
    }

    public List<String> getDisplayColumns() {
        return this.displayColumns;
    }

    /**
     * put参数
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        paramMap.put(key, value);
    }

    public void putAll(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        paramMap.putAll(map);
    }

    public Map<String, Object> getParamtMap() {
        return this.paramMap;
    }

    /**
     * 返回 queryId 的值
     * 
     * @return queryId
     */

    public String getQueryId() {
        return queryId;
    }

    /**
     * 设置 queryId 的值
     * 
     * @param queryId
     */

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

}
