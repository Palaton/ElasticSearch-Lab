package com.aist.common.rapidQuery.configParse.config;

/**
 * 列定义
 * 
 * @author HuangSiwei
 * @create 2014年8月25日 上午9:22:56
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Column {
    /**
     * 指定的列
     */
    private String             field;

    private String             name;
    
    private String             title;

    private boolean            hidden       = false;

    private String             customDict;

    /**
     * 导出的格式
     * 比如：yyyy-mm-dd
     */
    private String             expFmt;

    /**
     * 导出的数据类型：Date,Integer,Double 等等
     */
    private String 			   expType;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getCustomDict() {
        return customDict;
    }

    public void setCustomDict(String customDict) {
        this.customDict = customDict;
    }

    public String getExpFmt() {
        return expFmt;
    }

    public void setExpFmt(String expFmt) {
        this.expFmt = expFmt;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

}
