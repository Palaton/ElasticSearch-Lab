package com.aist.common.rapidQuery.dataQuery;

import com.aist.common.rapidQuery.scriptParse.BaseNode;

import java.util.List;
import java.util.Map;

public abstract class AbstractDataQuery implements DataQuery {
    
    protected String getScript(List<BaseNode> nodeList,Map<String, Object> prameter) {
        if(null ==  nodeList || nodeList.isEmpty() ){
            return null;
        }
        StringBuffer sbSql = new StringBuffer();
        for (BaseNode baseNode : nodeList) {
            baseNode.apply(prameter, sbSql);
        }
        
        //去除空行
        String sql = sbSql.toString().replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        
        return sql;
    }
    
}
