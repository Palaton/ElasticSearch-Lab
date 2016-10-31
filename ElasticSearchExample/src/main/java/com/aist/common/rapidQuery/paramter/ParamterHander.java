package com.aist.common.rapidQuery.paramter;

import com.aist.common.exception.BusinessException;
import com.aist.uam.auth.remote.vo.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParamterHander {

    private final static Logger LOGGER = LoggerFactory.getLogger(ParamterHander.class);

    public static Map<String, String[]> getParameters(ServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames == null) {
            return null;
        }

        Map<String, String[]> params = new HashMap<String, String[]>();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            paramName = paramName.endsWith("[]") ? paramName.substring(0, paramName.length() - 2)
                : paramName;
            params.put(paramName, values);
        }

        return params;
    }

    public static void mergeParamter(Map<String, String[]> subParams,
                                     Map<String, Object> paramter) {
        if (null != subParams) {
            for (String key : subParams.keySet()) {
                String[] val = subParams.get(key);
                if (null == val) {
                    continue;
                }
                if (val.length == 1) {
                    paramter.put(key, val[0]);
                    continue;
                }
                paramter.put(key, val);
            }
        }
    }

    public static Map<String, Object> mergeSysParamter(GridParam gp, SessionUser sessionUser){
        
      //得到参数列表
        Map<String, Object> sysParamter = getSessionMap(sessionUser);
        Map<String, Object> paramter = new HashMap<String, Object>();
        
        //分页参数
        paramter.putAll(addPageParamter(gp));
        
        paramter.putAll(gp.getParamtMap());
        
        if (!paramter.isEmpty() && null != sysParamter) {
            String msg = "快速查询queryid:" + gp.getQueryId()+ "， 使用了系统变量：";
            validateParamter(paramter.keySet(), sysParamter.keySet(), msg);
            paramter.putAll(sysParamter);
        }
        
        return paramter;
    }
    
    private static Map<String,Object> addPageParamter(GridParam gp){
        Map<String,Object> pageParam = new HashMap<String,Object>();
        int pageSize = gp.isCountOnly() ? 1 : gp.getRows();
        int pageNumber = gp.getPage();
        int start = (pageNumber - 1) * pageSize;
        pageParam.put(PageParam.pageSize.toString(), pageSize);
        pageParam.put(PageParam.pageNumber.toString(), pageNumber);
        pageParam.put(PageParam.start.toString(),start);
        return pageParam;
    }
    
    private static void validateParamter(Set<String> searchKey, Set<String> sysKey, String msg) {

        if (null == searchKey || null == sysKey) {
            return;
        }

        for (String str : searchKey) {
            if (sysKey.contains(str)) {
                throw new BusinessException(msg + str);
            }
        }
    }
    
    private static Map<String, Object> getSessionMap(SessionUser user) {
        if(user == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put(SysVarKey.SYS_USER_ID.toString(), user.getId());
            map.put(SysVarKey.SYS_USER_CODE.toString(), user.getEmployeeCode());
            map.put(SysVarKey.SYS_USER_LOGIN_NAME.toString(), user.getUsername());
            map.put(SysVarKey.SYS_USER_REAL_NAME.toString(), user.getRealName());
            map.put(SysVarKey.SYS_USER_HR_ID.toString(), user.getEmployeeCode());
            map.put(SysVarKey.SYS_ORG_ID.toString(), user.getServiceDepId());

            map.put(SysVarKey.SYS_BUSIDEPID.toString(), user.getBusiDepId());
            map.put(SysVarKey.SYS_BUSIBAID.toString(), user.getBusibaId());
            map.put(SysVarKey.SYS_BUSIWZID.toString(), user.getBusiwzId());
            map.put(SysVarKey.SYS_BUSISWZID.toString(), user.getBusiswzId());
            map.put(SysVarKey.SYS_BUSIARID.toString(), user.getBusiarId());
            map.put(SysVarKey.SYS_BUSIGRPID.toString(), user.getBusigrpId());
            map.put(SysVarKey.SYS_SUBGRPID.toString(), user.getSubgrpId());

            map.put(SysVarKey.SYS_JOBCODE.toString(), user.getServiceJobCode());

        } catch (Exception e) {
            LOGGER.error("获取session中的变量出错，可能导致sql执行不准确。" + e.getMessage(), e);
        }
        return map;
    }
}
