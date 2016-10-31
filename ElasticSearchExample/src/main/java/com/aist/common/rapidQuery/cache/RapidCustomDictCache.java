package com.aist.common.rapidQuery.cache;


import com.aist.common.rapidQuery.service.CustomDictService;
import com.aist.common.rapidQuery.utils.MergeTools;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Map;

public class RapidCustomDictCache implements RapidQueryCache {

    private final Logger LOGGER       = LoggerFactory.getLogger(this.getClass());

    CacheManager cacheManager = null;

    public RapidCustomDictCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Map<String, Map<String, Object>> findCustomDictCache(List<Map<String, Object>> keys,
                                                                CustomDictService dict,
                                                                String[] cols, String field,
                                                                String cacheName,
                                                                Map<String, Object> paramMap) {
        if (null == keys || keys.isEmpty()) {
            return null;
        }

        Boolean callDict = false;
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            callDict = true;
        }

        Map<String, Map<String, Object>> cacheValueMap = new CaseInsensitiveMap<String, Map<String, Object>>();
        if (dict.isCacheable()) {
            callDict = buildCacheValueMap(keys, cols, callDict, cache, cacheValueMap);
        } else {
            callDict = true;
        }

        Map<String, Map<String, Object>> dictValueMap = null;
        if (callDict) {
            dictValueMap = MergeTools.getDictValueMap(keys, dict, cols, field, paramMap);
        } else {
            dictValueMap = cacheValueMap;
        }

        //更新缓存
        if (callDict && dict.isCacheable()) {
            updateCache(dictValueMap, cacheValueMap, cache);
        }

        return dictValueMap;
    }

    private Boolean buildCacheValueMap(List<Map<String, Object>> keys, String[] cols,
                                       Boolean callDict, Cache cache,
                                       Map<String, Map<String, Object>> cacheValueMap) {
        for (Map<String, Object> key : keys) {

            String colKey = MergeTools.buildColKey(key, cols);

            ValueWrapper vw = cache.get(colKey.toUpperCase());
            if (null == vw) {
                callDict = true;
                cacheValueMap.put(colKey.toUpperCase(), null);
                continue;
            }

            Object val = vw.get();
            cacheValueMap.put(colKey, null == val ? null : (Map<String, Object>) val);
        }
        return callDict;
    }

    private void updateCache(Map<String, Map<String, Object>> dictValueMap,
                             Map<String, Map<String, Object>> cacheValueMap, Cache cache) {
        for (String cacheKey : cacheValueMap.keySet()) {
            if (null != dictValueMap && dictValueMap.containsKey(cacheKey)) {
                cache.put(cacheKey.toUpperCase(), dictValueMap.get(cacheKey));
                Object obj1 = cache.get(cacheKey.toUpperCase());
            } else {
                cache.put(cacheKey, null);
            }
        }

    }

}
