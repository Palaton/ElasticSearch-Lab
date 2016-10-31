package com.aist.common.rapidQuery.service;

import com.aist.common.rapidQuery.paramter.GridParam;
import com.aist.uam.auth.remote.vo.SessionUser;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface RapidQueryService {
    /**
     * 查询接口
     * 
     * @param gp
     * @param sessionUser
     * @return
     */
    Page<Map<String, Object>> findPage(GridParam gp, SessionUser sessionUser);
    
    /**
     * 查询接口（和session user无关）
     * 
     * @param gp
     * @return
     */
    default Page<Map<String, Object>> findPageWithOutSessionUser(GridParam gp){
        return findPage(gp,null);
    }
    
}
