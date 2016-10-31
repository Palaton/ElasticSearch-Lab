package com.aist.common.rapidQuery.utils;

import com.aist.common.quickQuery.service.CustomDictService;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapidMergeTools {

	/**
	 * 分隔符
	 */
	private final static String SEPARATE = "$$$";

	public static String buildColKey(Map<String, Object> item, String[] cols) {
		StringBuffer key = new StringBuffer();
		for (String ref : cols) {
			Object obj = item.get(ref);
			key.append(String.valueOf(obj)).append(SEPARATE);
		}
		return key.toString();
	}

	public static Map<String, Map<String, Object>> getDictValueMap(List<Map<String, Object>> keys,
			CustomDictService dict, String[] cols,String field, Map<String, Object> paramMap) {
		List<Map<String, Object>> dictValList = dict.findDicts(keys,paramMap);
		if(null == dictValList){
			return null;
		}
		String[] fieldNames = field.split(",");
		Map<String, Map<String, Object>> valueMap = new CaseInsensitiveMap<String, Map<String, Object>>();
		for (Map<String, Object> dictVal : dictValList) {
			Map<String, Object> dictValMap = new HashMap<String, Object>();
			String colkey = RapidMergeTools.buildColKey(dictVal, cols);
			if(fieldNames.length == 1){
				buildDictValMap(dictVal, dictValMap, CustomDictService.DICTVALCOL);
			}else {
				for (String fieldName : fieldNames) {
					buildDictValMap(dictVal, dictValMap, fieldName);
				}
			}
			valueMap.put(colkey, dictValMap);
		}
		return valueMap;
	}
	
	private static void buildDictValMap(Map<String, Object> dictVal,Map<String, Object> dictValMap,String fieldName){
		Object obj = dictVal.get(fieldName);
		dictValMap.put(fieldName, obj);
	}

	public static void mergeContext(List<Map<String, Object>> context, String field,
		Map<String, Map<String, Object>> valueMap, String[] cols) {
		if(null == context || null == valueMap || StringUtils.isBlank(field)){
			return ;
		}
		String[] fieldNames = field.split(",");
		context.forEach(item -> {
			String key = RapidMergeTools.buildColKey(item, cols);
			Map<String, Object> fieldValue = valueMap.get(key.toString());
			
			if(fieldNames.length == 1){
				putDictValue(field, item, fieldValue,CustomDictService.DICTVALCOL);
			}else{
				for (String fieldName : fieldNames) {
					putDictValue(fieldName, item, fieldValue,fieldName);
				}
			}
		});
	}

	private static void putDictValue(String field, Map<String, Object> item, Map<String, Object> fieldValue,String dictValCol) {
		if (null != fieldValue && null != fieldValue.get(dictValCol)) {
			item.put(field, fieldValue.get(dictValCol));
		}else{
			item.put(field,"");
		}
	}
	
	

}
