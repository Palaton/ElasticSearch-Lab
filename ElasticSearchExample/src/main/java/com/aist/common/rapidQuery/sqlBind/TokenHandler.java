package com.aist.common.rapidQuery.sqlBind;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: TokenHandler.java, v 0.1 2016年5月9日 上午9:26:47 ouyq Exp $
 */
public interface TokenHandler {
  String handleToken(String content, MapSqlParameterSource sqlParameterSource);
}