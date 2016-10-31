package com.aist.common.rapidQuery.service;

import java.util.List;
import java.util.Map;

/**
 * 自定义快速查询字典服务
 * @author linjiarong
 *
 */
public interface CustomDictService {
	
	/**
	 * 返回列value名字
	 */
	public final String DICTVALCOL = "val";
	
    /**
     * 根据KEY值进行转换
     * @param key
     * @return
     */
	@Deprecated
    default String getValue(String key) {
        return "";
    }

    /**
     * 多个值选择
     * 
     * @param key
     * @return
     */
	@Deprecated
    default String getValue(String[] key) {
        return "";
    }
    
    /**
     * CustomDict batch 查询
     * 例子:
     * <column type="virtual" name="SELLER" referencecol="name,code"/>
     * 传入参数: [{name:n1,code:c1},{name:n2,code:c2},{name:n3,code:c3}]
     * 传出参数：  [{name:n1,code:c1,val:v1},{name:n2,code:c2,val:v2},{name:n3,code:c3,val:v3}]
     * 		其中"val"是常量 CustomDictService.DICTVALCOL的值,不能修改。val的值就是SELLER列需要的值。
     * 
     * 一次性查出所有的dicts
     * @return
     */
    default List<Map<String,Object>> findDicts(List<Map<String, Object>> keys){
    	return null;
    }
    
    /**
     * CustomDict batch 查询
     * 
     * @param keys CustomDict batch 参数
     * @param paramMap 快速查询参数
     * @return
     */
    default List<Map<String,Object>> findDicts(List<Map<String, Object>> keys, Map<String, Object> paramMap){
        return findDicts(keys);
    }
    
    /**
     * 只有返回true的时候才会总batch查询
     * 
     * @return
     */
    default Boolean getIsBatch(){
    	return false;
    }
    
    default Boolean isCacheable(){
    	return true;
    }
}
