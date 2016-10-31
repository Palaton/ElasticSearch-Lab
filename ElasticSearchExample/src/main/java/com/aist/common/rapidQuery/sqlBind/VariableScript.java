package com.aist.common.rapidQuery.sqlBind;

import com.aist.common.rapidQuery.scriptParse.ExpressionEvaluator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: VariableSql.java, v 0.1 2016年10月11日 下午2:39:28 ouyq Exp $
 */
public class VariableScript {

    public static final String PRIFIX_SHARP    = "#{";
    public static final String PRIFIX_DOLLAR   = "${";
    public static final String SUBFIX          = "}";
    public static final String DOLLAR          = "$";
    public static final String SHARP           = "#";
    public static final String PRIFIX_SPEL_VAR = "__SPEL_";

    public static String bindVariable(MapSqlParameterSource ms, Map<String, Object> paramMap,
                                      String sql) {
        String bindSql = "";
        bindSql = bindVariableForRegx(ms, paramMap, sql, PRIFIX_SHARP, SUBFIX);
        bindSql = bindVariableForRegx(ms, paramMap, bindSql, PRIFIX_SHARP, SUBFIX);
        return bindSql;
    }

    public static String bindVariableForRegx(MapSqlParameterSource ms, Map<String, Object> paramMap,
                                             String sql, String openToken, String closeToken) {
        GenericTokenParser parse = new GenericTokenParser(openToken, closeToken, ms,
            new TokenHandler() {
                @Override
                public String handleToken(String content, MapSqlParameterSource ms) {

                    String[] param = content.split(",");
                    String varName = param[0];

                    Object value = null;
                    if (!varName.contains(SHARP)) {
                        value = paramMap.get(varName);
                    } else {
                        value = ExpressionEvaluator.spelEvalute(varName, paramMap);
                        varName = PRIFIX_SPEL_VAR + ms.getValues().size();
                    }

                    if (openToken.startsWith(DOLLAR)) {
                        return null == value ? "" : String.valueOf(DOLLAR);
                    }

                    JDBCType jt = null;
                    if (param.length > 1) {
                        jt = JDBCType.valueOf(param[1]);
                    } else {
                        jt = JDBCType.VARCHAR;
                    }

                    ms.addValue(varName, value, jt.getVendorTypeNumber());
                    return ":" + varName;
                }
            });
        return parse.parse(sql);
    }

    public static void main(String[] args) {
        String sql = "SYS_PARAM_DEFINE d WHERE v.is_deleted= 0 and d.IS_DELETED = 0 and v.DEF_ID = d.ID "
                     + "and d.name = #{'#abc',VARCHAR}";
        MapSqlParameterSource ms = new MapSqlParameterSource();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("abc", "张三");
        sql = bindVariable(ms, map, sql);
        System.out.println(sql);
    }
}
