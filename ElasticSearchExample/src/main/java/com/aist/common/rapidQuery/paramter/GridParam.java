package com.aist.common.rapidQuery.paramter;

/**
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author HuangSiwei
 * @create 2014年9月19日 下午5:01:11
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

public class GridParam extends AbstractGridParam {

    private int    page;
    private int    rows;
    private String order; // asc,desc
    private String sort;  // 排序的字段
    /**
     * 是否进行分页查询
     */
    private boolean pagination = true;
    /** 
     * 只查询出count,不查询列表数据
     */
    private boolean countOnly = false;
    
    /**
     * 返回 page 的值
     * 
     * @return page
     */

    public int getPage() {
        return page;
    }

    /**
     * 设置 page 的值
     * 
     * @param page
     */

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 返回 rows 的值
     * 
     * @return rows
     */

    public int getRows() {
        return rows;
    }

    /**
     * 设置 rows 的值
     * 
     * @param rows
     */

    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * 返回 order 的值
     * 
     * @return order
     */

    public String getOrder() {
        return order;
    }

    /**
     * 设置 order 的值
     * 
     * @param order
     */

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 返回 sort 的值
     * 
     * @return sort
     */

    public String getSort() {
        return sort;
    }

    /**
     * 设置 sort 的值
     * 
     * @param sort
     */

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public boolean isCountOnly() {
        return countOnly;
    }

    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }

}
