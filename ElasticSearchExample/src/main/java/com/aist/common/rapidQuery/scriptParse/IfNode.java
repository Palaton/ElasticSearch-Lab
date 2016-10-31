package com.aist.common.rapidQuery.scriptParse;

import java.util.Map;

/**
 * 
 * @author ouyq
 * @version $Id: IfSqlNode.java, v 0.1 2016年5月4日 下午5:33:47 ouyq Exp $
 */
public class IfNode implements BaseNode {
	
	/**表达式 */
	private String test = "";
	private BaseNode contents;
	
	public IfNode(BaseNode contents, String test) {
	    this.test = test;
	    this.contents = contents;
	}

	public boolean apply(Map<String, Object> param, StringBuffer sbSql) {
		if (ExpressionEvaluator.spelEvaluteBoolean(test, param)) {
		    contents.apply(param,sbSql);
			return true;
		}
		return false;
	}

}
