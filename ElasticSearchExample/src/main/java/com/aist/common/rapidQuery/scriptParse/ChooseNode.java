package com.aist.common.rapidQuery.scriptParse;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: ChooseSqlNode.java, v 0.1 2016年5月9日 上午9:33:37 ouyq Exp $
 */
public class ChooseNode implements BaseNode {
    private BaseNode defaultSqlNode;
    private List<BaseNode> ifSqlNodes;

    public ChooseNode(List<BaseNode> ifSqlNodes, BaseNode defaultSqlNode) {
        this.ifSqlNodes = ifSqlNodes;
        this.defaultSqlNode = defaultSqlNode;
    }

    @Override
    public boolean apply(Map<String, Object> param, StringBuffer sbSql) {
        for (BaseNode sqlNode : ifSqlNodes) {
            if (sqlNode.apply(param, sbSql)) {
                return true;
            }
        }
        if (defaultSqlNode != null) {
            defaultSqlNode.apply(param, sbSql);
            return true;
        }
        return false;
    }
}
