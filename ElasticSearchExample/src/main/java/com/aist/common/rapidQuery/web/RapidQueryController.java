package com.aist.common.rapidQuery.web;

import com.aist.common.quickQuery.web.vo.DisplayColomn;
import com.aist.common.rapidQuery.export.ExcelExport;
import com.aist.common.rapidQuery.paramter.GridParam;
import com.aist.common.rapidQuery.paramter.ParamterHander;
import com.aist.common.rapidQuery.service.RapidQueryService;
import com.aist.common.rapidQuery.web.vo.DatagridVO;
import com.aist.uam.auth.remote.UamSessionService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rapidQuery")
public class RapidQueryController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RapidQueryService rapidQueryService;
    
    @Autowired
    private UamSessionService uamSessionService;
    
    @RequestMapping(value = "findPage")
    @ResponseBody
    public DatagridVO findPage(GridParam gridParam, HttpServletRequest request) {
        if (null != gridParam && gridParam.getRows() > 100) {
            gridParam.setRows(100);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter QuickQuery findPage, queryId:" + gridParam.getQueryId());
        }
        Page<Map<String, Object>> page = service(gridParam, request);
        DatagridVO result = new DatagridVO(page);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Leave QuickQuery findPage, queryId:" + gridParam.getQueryId());
        }
        return result;
    }

    @RequestMapping(value = "findPage", params = "xlsx")
    @ResponseBody
    public void findPageExcel(HttpServletRequest request, HttpServletResponse response,
                              GridParam gridParam, DisplayColomn displayColomn) {
        LOGGER.debug(" QuickQuery findPage excel export!");
        String queryId = request.getParameter("queryId");
        if (StringUtils.isBlank(queryId)) {
            LOGGER.error("queryId 不能为空！");
            return;
        }

        Page<Map<String, Object>> pages = getExcelData(gridParam, request,
            displayColomn.getColomns());
        LOGGER.debug("RapidQuery finish fetch excel data!");
        ExcelExport export = new ExcelExport();
        Workbook wordkBook = export.buildExcelDocument(pages, displayColomn.getColomns(), queryId);
        LOGGER.debug("RapidQuery finish generate excel workbook!");
        export.exportExcel(wordkBook, response);
    }
    
    private Page<Map<String, Object>> getExcelData(GridParam gridParam,
                                                   HttpServletRequest request,
                                                   List<String> displayColumns) {
        gridParam.setPagination(false);
        gridParam.addAllDisplayColumns(displayColumns);
        Page<Map<String, Object>> pages = service(gridParam, request);
        return pages;
    }
    
    private Page<Map<String, Object>> service(GridParam gp, HttpServletRequest request) {
        
        Map<String, String[]> paramMap = ParamterHander.getParameters(request);
        
        Map<String, Object> paramObj = new HashMap<String, Object>();
        ParamterHander.mergeParamter(paramMap,paramObj);
        
        gp.putAll(paramObj);
        
        return rapidQueryService.findPage(gp, uamSessionService.getSessionUser());
    }

}
