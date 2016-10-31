package com.aist.common.rapidQuery.scriptParse;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: MixedNode.java, v 0.1 2016年10月13日 下午3:12:14 ouyq Exp $
 */
public class MixedNode implements BaseNode {
  private List<BaseNode> contents;

  public MixedNode(List<BaseNode> contents) {
    this.contents = contents;
  }

  @Override
  public boolean apply(Map<String, Object> param, StringBuffer sbSql) {
    for (BaseNode sqlNode : contents) {
      sqlNode.apply(param,sbSql);
    }
    return true;
  }
}
