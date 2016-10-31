package com.aist.common.rapidQuery.configParse.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author HuangSiwei
 * @create 2014年9月3日 下午2:54:02
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class RapidQuerysContext {
    private boolean            debug          = true;
    private List<String>       configFileList = new ArrayList<String>();
    public Lock                LOCK           = new ReentrantLock();
    private Map<String, Query> queryMap       = new ConcurrentHashMap<String, Query>();
    
    /**
     * 
     * 构造函数
     */
    private RapidQuerysContext() {
    }

    /**
     * 
     * 单例
     * 
     * @author HuangSiwei
     * @create 2014年9月3日 下午2:53:59
     * @version 1.0.0
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    private static class SingletonHolder {
        public final static RapidQuerysContext instance = new RapidQuerysContext();
    }

    /**
     * 
     * 获取实例
     * 
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public static RapidQuerysContext getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public boolean isParsed() {
        try {
            LOCK.lock();
            return queryMap != null && !queryMap.isEmpty();
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @param query
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public void add(Query query) {
        queryMap.put(query.getId(), query);
    }

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @param id
     * @return
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public Query getQuery(String id) {
        return queryMap.get(id);
    }

    /**
     * 返回 configFileList 的值
     * 
     * @return configFileList
     */
    public List<String> getConfigFileList() {
        try {
            LOCK.lock();
            return configFileList;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 设置 configFileList 的值
     * 
     * @param configFileList
     */

    public void addConfigFile(String configFile) {

        try {
            LOCK.lock();
            this.configFileList.add(configFile);
        } finally {
            LOCK.unlock();
        }
    }
    
    public void addAllConfigFile(List<String> configList) {
        if(configList == null || configList.isEmpty()){
            return;
        }
        try {
            LOCK.lock();
            this.configFileList.addAll(configList);
        } finally {
            LOCK.unlock();
        }
    }

    public void clear() {
        queryMap.clear();
    }

    /**
     * 返回 debug 的值
     * 
     * @return debug
     */

    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置 debug 的值
     * 
     * @param debug
     */

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
