package com.aist.common.rapidQuery.scriptParse;

import java.util.Map;

/**
 * 最基本的node
 * 
 * @author ouyq
 * @version $Id: SqlNode.java, v 0.1 2016年5月4日 下午5:28:40 ouyq Exp $
 */
public interface BaseNode {
	boolean apply(Map<String, Object> param, StringBuffer sbSql);
}
