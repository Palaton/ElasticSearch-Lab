package com.aist.common.rapidQuery.export;

import com.aist.common.rapidQuery.configParse.config.Column;
import com.aist.common.rapidQuery.configParse.config.Query;
import com.aist.common.rapidQuery.configParse.config.RapidQuerysContext;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelExport {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private List<Column> getQueryColumns(String queryId) {
        RapidQuerysContext queryContext = RapidQuerysContext.getInstance();
        Query query = queryContext.getQuery(queryId);
        List<Column> columns = query.getColumnList();
        return columns;
    }

    public Workbook buildExcelDocument(Page<Map<String, Object>> pages,
                                        List<String> displayColumns, String queryId) {
        List<Column> queryColumns = getQueryColumns(queryId);
        Map<String, Column> head = getExcelHead(displayColumns, queryColumns);

        Workbook workBook = new SXSSFWorkbook();

        //设置日期cellStyle
        DataFormat format = workBook.createDataFormat();
        CellStyle dateCellStyle = workBook.createCellStyle();
        dateCellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));

        //产生工作表对象  
        Sheet sheet = workBook.createSheet();

        //add head
        if (null == head || head.isEmpty()) {
            return workBook;
        }

        Row row = sheet.createRow(0);
        Set<String> headSet = head.keySet();
        int headNum = 0;
        for (String key : headSet) {
            Cell cell = row.createCell(headNum);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            String headValue = null == head.get(key) ? key : head.get(key).getTitle();
            cell.setCellValue(headValue);
            headNum = headNum + 1;
        }

        //add data
        if (null == pages) {
            return workBook;
        }
        Iterator<Map<String, Object>> iter = pages.iterator();
        int rowNum = 1;
        while (iter.hasNext()) {
            Map<String, Object> dataMap = iter.next();
            Row dataRow = sheet.createRow(rowNum);
            rowNum = rowNum + 1;
            int cellNum = 0;
            for (String key : headSet) {
                Cell cell = dataRow.createCell(cellNum);

                cellNum = cellNum + 1;

                Object cellValue = dataMap.get(key);
                if (cellValue == null) {
                    continue;
                }
                String expType = head.get(key).getExpType();
                String expFmt = head.get(key).getExpFmt();
                if ("date".equalsIgnoreCase(expType) && StringUtils.isNotBlank(expFmt)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(expFmt);
                    dateCellStyle.setDataFormat(format.getFormat(expFmt));
                    cell.setCellStyle(dateCellStyle);
                    try {
                        if (cellValue != null && !StringUtils.isBlank(String.valueOf(cellValue))) {
                            cell.setCellValue(sdf.parse(String.valueOf(cellValue)));
                        }
                    } catch (ParseException e) {
                        LOGGER.info(e.getMessage(), e);
                    }
                } else if ("Integer".equalsIgnoreCase(expType)
                           || cellValue.getClass() == Integer.class) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Integer.valueOf(cellValue + ""));
                } else if ("Double".equalsIgnoreCase(expType)
                           || cellValue.getClass() == Double.class) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    expFmt = StringUtils.isBlank(expFmt) ? "#,###.00" : expFmt;
                    Format fm1 = new DecimalFormat(expFmt);
                    Object val = "";
                    try {
                        val = fm1.parseObject(cellValue + "");
                    } catch (ParseException e) {
                        LOGGER.error("string转换为double错误", e);
                    }
                    cell.setCellValue(Double.valueOf(val + ""));
                } else if (cellValue.getClass() == Date.class) {
                    cell.setCellStyle(dateCellStyle);
                    cell.setCellValue((Date) cellValue);
                } else {
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(cellValue));
                }

            }
        }

        return workBook;
    }

    private Map<String, Column> getExcelHead(List<String> displayColumns,
                                             List<Column> queryColumns) {
        Map<String, Column> headMap = new LinkedHashMap<String, Column>();
        if (null == queryColumns || null == displayColumns) {
            return headMap;
        }

        Map<String, Column> map = new HashMap<String, Column>();
        try {
            for (Column col : queryColumns) {
                if (StringUtils.isBlank(col.getName())) {
                    continue;
                }
                String[] names = col.getName().split(",");
                String[] titles = StringUtils.isBlank(col.getTitle()) ? col.getName().split(",")
                    : col.getTitle().split(",");
                if (titles.length != names.length) {
                    LOGGER.error("导出列的name和title不相等，name：" + col.getName());
                    continue;
                }
                for (int i = 0; i < names.length; i++) {
                    Column c = new Column();
                    BeanUtils.copyProperties(c, col);
                    c.setTitle(titles[i]);
                    map.put(names[i], c);
                }
            }
        } catch (Exception e) {
            LOGGER.error("复制column数据出错", e);
        }

        for (String name : displayColumns) {
            if (map.containsKey(name)) {
                headMap.put(name, map.get(name));
            }
        }
        return headMap;
    }
    
    public void exportExcel(Workbook wordkBook, HttpServletResponse response) {
        // 生成提示信息，  
        response.setContentType("application/vnd.ms-excel");
        OutputStream fOut = null;
        try {
            response.setHeader("content-disposition",
                "attachment;filename=" + getFileName() + ".xlsx");
            fOut = response.getOutputStream();
            wordkBook.write(fOut);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.info(e.getMessage());
            }
        }
    }
    
    private String getFileName() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(date);
    }
}
