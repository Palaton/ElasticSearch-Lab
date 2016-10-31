package com.aist.common.rapidQuery.cache;


import com.aist.common.rapidQuery.service.CustomDictService;

import java.util.List;
import java.util.Map;

/**
 * 快速查询缓存
 * 
 * @author ouyq
 * @version $Id: QuickQueryCache.java, v 0.1 2016年4月19日 下午3:51:38 ouyq Exp $
 */
public interface RapidQueryCache {

    
	/**
	 * 得到customdict 缓存
	 * 
	 * @param keys
	 * @param dict
	 * @param cols
	 * @param field
	 * @param queryId
	 * @return
	 */
	Map<String,Map<String,Object>> findCustomDictCache(List<Map<String, Object>> keys,
													   CustomDictService dict, String[] cols, String field, String queryId, Map<String, Object> paramMap);

}
