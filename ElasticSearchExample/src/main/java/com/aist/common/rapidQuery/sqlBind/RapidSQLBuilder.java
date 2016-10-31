package com.aist.common.rapidQuery.sqlBind;

import com.aist.common.exception.BusinessException;
import com.aist.common.rapidQuery.paramter.PageParam;
import com.aist.common.sqlparser.SqlparserOpreationService;
import com.aist.common.sqlparser.impl.SqlparserStatementServiceImpl;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: RapidSQLBuilder.java, v 0.1 2016年10月10日 下午4:17:39 ouyq Exp $
 */
public class RapidSQLBuilder {

    private static SqlparserOpreationService sqlparserStatementService = new SqlparserStatementServiceImpl();

    public static String getCountSQL(String baseSql, Boolean performenceCountSql) {
        if (null == baseSql) {
            return null;
        }
        baseSql = baseSql.trim();
        
        Integer orderByidx = getOrderByIndex(baseSql);
        if(orderByidx > -1){
            baseSql = baseSql.substring(0,orderByidx);
        }
        
        if (baseSql.toUpperCase().startsWith("WITH")) {
            return buildCountSqlForCTE(baseSql);
        }
        String sqlBegin = "select count(1) from(";
        String sqlEnd = ") t";
        String countSql = sqlBegin + baseSql + sqlEnd;
        // 优化COUNTSQL
        if (performenceCountSql) {
            try {
                countSql = sqlparserStatementService.performenceForCountSql(countSql);
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
        }
        return countSql;
    }

    private static String buildCountSqlForCTE(String sql) {

        Integer cteEnd = sql.indexOf(")");

        String cteSql = sql.substring(0, cteEnd + 1);

        String countSql = sql.substring(cteEnd + 1);

        String sqlBegin = "select count(1) from(";
        String sqlEnd = ") t";

        return cteSql + sqlBegin + countSql + sqlEnd;
    }

    public static String getPaginationSQL(String sql, Map<String, Object> paramMap) {

        if (StringUtils.isBlank(sql)) {
            return sql;
        }

        sql = sql.trim();

        String orderBySql = getOrderBySql(sql, paramMap);

        String pageSql = orderBySql + "  OFFSET #{" + PageParam.start
                         + ",INTEGER} ROWS FETCH  NEXT #{" + PageParam.pageSize
                         + ",INTEGER} ROWS ONLY";
        return pageSql;
    }

    private static String getFirstColumn(String baseSql) {
        if (null == baseSql || "".equals(baseSql.trim())) {
            return null;
        }
        baseSql = baseSql.replace("DISTINCT", "");
        Integer selectIdx = Math.min(baseSql.indexOf("select"), baseSql.indexOf("SELECT"));
        Integer fromIdx = Math.max(baseSql.indexOf("from"), baseSql.indexOf("FROM"));
        String[] clomns = baseSql.substring(selectIdx + "SELECT".length() + 1, fromIdx).split(",");

        String clomnFirst = clomns[0].trim().split(" ")[0];

        return clomnFirst;
    }

    private static String getSubOrderBySqlFromParamter(Map<String, Object> map) {
        Object sort = map.get(PageParam.sort.toString());
        if (sort == null || StringUtils.isBlank(sort + "")) {
            return null;
        }
        String[] sorts = String.valueOf(sort).split(",");

        StringBuffer orderBySql = new StringBuffer("order by ");

        Object order = map.get(PageParam.order.toString());
        if (order != null && StringUtils.isBlank(order + "")) {
            String[] orders = String.valueOf(order).split(",");
            if (sorts.length != orders.length) {
                throw new BusinessException(" 排序字段和排序规则长度不一致。queryId：" + map.get("queryId")
                                            + "  sort:" + sort + "  order:" + order);
            }

            for (int i = 0; i < sorts.length; i++) {
                orderBySql.append(sorts[i]);
                orderBySql.append(" ");
                orderBySql.append(orders[i]);
                if (i < sorts.length - 1) {
                    orderBySql.append(", ");
                }
            }
            return orderBySql.toString();
        }

        for (int i = 0; i < sorts.length; i++) {
            orderBySql.append(sorts[i]);
            if (i < sorts.length - 1) {
                orderBySql.append(", ");
            }
        }

        return orderBySql.toString();

    }

    private static Integer getOrderByIndex(String sql) {
        String baseSql = sql.toUpperCase();
        Integer fromIdx = baseSql.lastIndexOf("FROM");
        if (fromIdx == -1) {
            return null;
        }

        Integer orderInd = baseSql.lastIndexOf("ORDER");
        return orderInd > fromIdx ? orderInd : -1;
    }

    public static String getOrderBySql(String sql, Map<String, Object> map) {

        if (StringUtils.isBlank(sql)) {
            return null;
        }

        //从参数得到sql
        String orderBySql = getSubOrderBySqlFromParamter(map);
        //判断原来sql有没有order by
        Integer orderIdx = getOrderByIndex(sql);
        
        if (orderIdx > 0) {
            if (StringUtils.isBlank(orderBySql)) {
                return sql;
            }
            sql = sql.substring(0, orderIdx);
            return sql + orderBySql;
        }

        if (StringUtils.isBlank(orderBySql)) {
            String firstCol = getFirstColumn(sql);
            return sql + " ORDER BY " + firstCol;
        }

        return sql + " " + orderBySql;
    }
}
