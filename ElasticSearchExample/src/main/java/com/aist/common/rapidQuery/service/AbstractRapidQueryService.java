package com.aist.common.rapidQuery.service;

import com.aist.common.exception.BusinessException;
import com.aist.common.quickQuery.service.CustomDictService;
import com.aist.common.rapidQuery.cache.RapidCustomDictCache;
import com.aist.common.rapidQuery.configParse.config.Column;
import com.aist.common.rapidQuery.configParse.config.Query;
import com.aist.common.rapidQuery.configParse.config.RapidQuerysContext;
import com.aist.common.rapidQuery.configParse.config.SearchTpye;
import com.aist.common.rapidQuery.dataQuery.DataQuery;
import com.aist.common.rapidQuery.dataQuery.impl.DBDataQuery;
import com.aist.common.rapidQuery.dataQuery.impl.ESDataQuery;
import com.aist.common.rapidQuery.paramter.GridParam;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.utils.RapidMergeTools;
import com.aist.common.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

public abstract class AbstractRapidQueryService implements RapidQueryService {

    private final Logger LOGGER = LoggerFactory.getLogger(AbstractRapidQueryService.class);

    //    @Autowired
    //    protected QuerysContext querysContext;

    RapidCustomDictCache customDictCache;

    DBDataQuery          dbDataQuery;

    ESDataQuery esDataQuery;

    public AbstractRapidQueryService(CacheManager cache, DataSource ds) {
        customDictCache = new RapidCustomDictCache(cache);
        dbDataQuery = new DBDataQuery(new NamedParameterJdbcTemplate(ds), cache);
    }

    protected void preQuery(GridParam gp, Map<String, Object> paramMap) {
        //查看权限
    }

    protected Query getQuery(String queryId) {
        RapidQuerysContext querysContext = RapidQuerysContext.getInstance();
        Query query = querysContext.getQuery(queryId);
        if(null == query){
            throw new BusinessException("找不到queryId:" + queryId);
        }
        return query;
    }

    protected DataQuery getDataQuery(Query query) {
        SearchTpye st = query.getSearchScript().getType();
        DataQuery dq = null;
        switch (st) {
            case ES:
                dq = esDataQuery;
                break;
            default:
                dq = dbDataQuery;
                break;
        }
        return dq;
    }

    protected String getSearchScript(List<BaseNode> sqlNodes, Map<String, Object> paramMap) {

        StringBuffer sbSql = new StringBuffer();
        for (BaseNode sqlNode : sqlNodes) {
            sqlNode.apply(paramMap, sbSql);
        }
        return sbSql.toString();

    }

    protected void formatData(List<Map<String, Object>> content, Map<String, Object> paramMap,
                              Query query) {
        if (content == null || content.isEmpty()) {
            return;
        }

        Set<String> colNames = new HashSet<String>();
        List<Column> cols = query.getColumnList();
        for (Column column : cols) {
            
            String[] colNameArray = column.getName().split(",");
            for (String name : colNameArray) {
                colNames.add(name);
            }
            
            CustomDictService cdService = null;
            String beanName = column.getCustomDict();
            if (null == beanName || StringUtils.isBlank(beanName)) {
                continue;
            } else {
                cdService = SpringUtils.getBean(beanName);
            }

            String[] refIds = column.getField().split(",");
            if (null == refIds || refIds.length < 1) {
                refIds = new String[] { column.getName() };
            }

            List<Map<String, Object>> keyList = buildKeyList(content, refIds);

            String cacheName = query.getId() + column.getName();

            Map<String, Map<String, Object>> valueMap = customDictCache.findCustomDictCache(keyList,
                cdService, refIds, column.getField(), cacheName, paramMap);
            
            if (null == valueMap || valueMap.isEmpty()) {
                return;
            }
            
            RapidMergeTools.mergeContext(content, column.getField(), valueMap, refIds);
        }
        
        //要删除的列
        Set<String> removeKey = new HashSet<String>();
        Set<String> keySet = content.get(0).keySet();
        removeKey.addAll(keySet);
        removeKey.removeAll(colNames);
        
        //去掉多余的列
        for (Map<String,Object> map : content) {
            for (String rk : removeKey) {
                map.remove(rk);
            }
        }

    }

    private List<Map<String, Object>> buildKeyList(List<Map<String, Object>> context,
                                                   final String[] cols) {
        List<Map<String, Object>> keyList = new ArrayList<Map<String, Object>>();
        context.forEach(item -> {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String col : cols) {
                Object obj = item.get(col);
                map.put(col, obj);
            }
            keyList.add(map);
        });
        return keyList;
    }
}
