package com.aist.common.rapidQuery.scriptParse;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TrimNode implements BaseNode {
    private String       prefix;
    private String       suffix;
    protected List<String> prefixesToOverride;
    private List<String> suffixesToOverride;
    private BaseNode     contents;

    public TrimNode(){
        
    }
    
    public TrimNode(String prefix, String suffix, String prefixOverrides, String suffixOverrides,
                    BaseNode contents) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.prefixesToOverride = parseOverrides(prefixOverrides);
        this.suffixesToOverride = parseOverrides(suffixOverrides);
        this.contents = contents;
    }

    @Override
    public boolean apply(Map<String, Object> param, StringBuffer sql) {
        StringBuffer subSql = new StringBuffer();
        contents.apply(param, subSql);

        StringBuffer trimSb = new StringBuffer(subSql.toString().trim());
        String upSubSql = trimSb.toString().toUpperCase();
        applyPrefix(trimSb, upSubSql);
        applySuffix(trimSb, upSubSql);
        sql.append(trimSb);

        return true;
    }

    private static List<String> parseOverrides(String overrides) {
        if (overrides != null) {
            final StringTokenizer parser = new StringTokenizer(overrides, "|", false);
            return new ArrayList<String>() {
                private static final long serialVersionUID = -2504816393625384165L;
                {
                    while (parser.hasMoreTokens()) {
                        add(parser.nextToken().toUpperCase(Locale.ENGLISH));
                    }
                }
            };
        }
        return Collections.emptyList();
    }

    private void applyPrefix(StringBuffer sql, String trimmedUppercaseSql) {
        if(StringUtils.isBlank(sql)){
            return ;
        }
        if (prefixesToOverride != null) {
            for (String toRemove : prefixesToOverride) {
                if (trimmedUppercaseSql.startsWith(toRemove)) {
                    sql.delete(0, toRemove.trim().length());
                    break;
                }
            }
        }
        if (prefix != null) {
            sql.insert(0, " ");
            sql.insert(0, prefix);
        }
    }

    private void applySuffix(StringBuffer sql, String trimmedUppercaseSql) {
        if(StringUtils.isBlank(sql)){
            return ;
        }
        if (suffixesToOverride != null) {
            for (String toRemove : suffixesToOverride) {
                if (trimmedUppercaseSql.endsWith(toRemove)
                    || trimmedUppercaseSql.endsWith(toRemove.trim())) {
                    int start = sql.length() - toRemove.trim().length();
                    int end = sql.length();
                    sql.delete(start, end);
                    break;
                }
            }
            if (suffix != null) {
                sql.append(" ");
                sql.append(suffix);
            }
        }
    }

}
