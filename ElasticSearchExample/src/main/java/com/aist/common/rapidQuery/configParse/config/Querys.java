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
public class Querys {

    private String      id;

    /**
     * 类型： ES,DB
     */
    private String      type;
    
    private List<Query> querys;

    /**
     * 返回 id 的值
     * 
     * @return id
     */

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Query> getQuerys() {
        return querys;
    }

    public void setQuerys(List<Query> querys) {
        this.querys = querys;
    }

    public void setId(String id) {
        this.id = id;
    }

}
