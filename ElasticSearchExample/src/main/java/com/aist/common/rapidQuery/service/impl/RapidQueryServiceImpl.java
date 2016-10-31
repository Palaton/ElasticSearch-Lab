package com.aist.common.rapidQuery.service.impl;

import com.aist.common.rapidQuery.configParse.config.Query;
import com.aist.common.rapidQuery.dataQuery.DataQuery;
import com.aist.common.rapidQuery.paramter.GridParam;
import com.aist.common.rapidQuery.paramter.ParamterHander;
import com.aist.common.rapidQuery.service.AbstractRapidQueryService;
import com.aist.uam.auth.remote.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;

import javax.sql.DataSource;
import java.util.Map;

public class RapidQueryServiceImpl extends AbstractRapidQueryService {

    public RapidQueryServiceImpl(CacheManager cacheManager, DataSource ds) {
        super(cacheManager,ds);
    }
    
    
    @Override
    public Page<Map<String, Object>> findPage(GridParam gp, SessionUser sessionUser) {
        
        
        Map<String,Object> paramter = ParamterHander.mergeSysParamter(gp, sessionUser);
        
        Query query = getQuery(gp.getQueryId());
        
        //权限校验
        validatePermission(query.getPermission());
        
        DataQuery dq = super.getDataQuery(query);
        
        Page<Map<String, Object>> page = null;
        if(gp.isPagination()){
            page = dq.query(query.getSearchScript(), paramter);
        }else{
            page = dq.queryNoPagination(query.getSearchScript(), paramter);
        }
        if(page == null){
            return null;
        }
        
        formatData(page.getContent(),paramter,query);
        
        return page;
    }
    
    private void validatePermission(String permission) {
        if (StringUtils.isEmpty(permission) || null == SecurityUtils.getSubject()) {
            return;
        }
        SecurityUtils.getSubject().checkPermission(permission);
    }
    
}
