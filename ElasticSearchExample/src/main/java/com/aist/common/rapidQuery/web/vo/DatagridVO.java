package com.aist.common.rapidQuery.web.vo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 向Web页面传送EasyUI的Datagrid数据，根据EasyUI Datagrid所要求的数据结构进行封装
 * @author linjiarong
 * @create 2014年8月28日 上午10:53:19 
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DatagridVO {

    /**
     * 当前页码
     */
    private Integer      page;

    /**
     * 数据总量(总页数)
     */
    private Integer      total;
    private Integer pagesize;
    /**
     * 数据内容
     */
    private List<Object> rows;

    /**
     * 数据总条数
     */
    private Long         records;

    /**
     * 构造函数 
     * 对SpringData的page对象进行转化为Datagrid可以识别的对象
     * @param pageData
     */
    public DatagridVO(Page pageData) {
        this.records = pageData.getTotalElements();
        this.page = pageData.getNumber();
        this.total = pageData.getTotalPages();
        this.pagesize=pageData.getSize();
        this.rows = pageData.getContent();
    }

    public List<Object> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }

    /**
     * Getter method for property <tt>page</tt>.
     * 
     * @return property value of page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Setter method for property <tt>page</tt>.
     * 
     * @param page value to be assigned to property page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * Getter method for property <tt>records</tt>.
     * 
     * @return property value of records
     */
    public Long getRecords() {
        return records;
    }

    /**
     * Setter method for property <tt>records</tt>.
     * 
     * @param records value to be assigned to property records
     */
    public void setRecords(Long records) {
        this.records = records;
    }

    /**
     * Getter method for property <tt>total</tt>.
     * 
     * @return property value of total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Setter method for property <tt>total</tt>.
     * 
     * @param total value to be assigned to property total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

        /**
         * Getter method for property <tt>pagesize</tt>.
         * 
         * @return property value of pagesize
         */
    public Integer getPagesize() {
        return pagesize;
    }

        /**
         * Setter method for property <tt>pagesize</tt>.
         * 
         * @param pagesize value to be assigned to property pagesize
         */
    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

}
