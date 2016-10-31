package com.aist.common.rapidQuery.scriptParse;

import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: IfSqlNode.java, v 0.1 2016年5月4日 下午5:33:47 ouyq Exp $
 */
public class TextNode implements BaseNode {
    
    private String text;
    
    public TextNode(String text){
        this.text =  text;
    }
    
	
	public boolean apply(Map<String, Object> param, StringBuffer sbSql) {
		sbSql.append(text);
		return true;
	}

}
