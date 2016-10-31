package com.aist.common.rapidQuery.scriptParse;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForEachNode implements BaseNode {

    public static final String ITEM_PREFIX         = "__frch_";
    private static final String PRIFIX_SHARP = "(\\$|#){1,}\\{";
    private static final String SUBFIX = "\\}";
    ExpressionEvaluator expressionEvaluator = null;

    private BaseNode contents;
    private String             collection;
    private String             open;
    private String             close;
    private String             separator;
    private String             item;
    private String             index;

    public ForEachNode(BaseNode contents, String collection, String open, String close,
                       String separator, String item, String index) {
        this.contents = contents;
        this.collection = collection;
        this.open = open;
        this.close = close;
        this.separator = separator;
        this.item = item;
        this.index = index;
        expressionEvaluator = new ExpressionEvaluator();
    }

    @Override
    public boolean apply(Map<String, Object> param, StringBuffer subSql) {
        Iterable<?> iterable = expressionEvaluator.evaluateIterable(collection, param);
        if (!iterable.iterator().hasNext()) {
            return true;
        }

        applyOpen(subSql);

        int i = 0;

        StringBuffer contentSql = new StringBuffer();
        contents.apply(param, contentSql);
        contentSql = new StringBuffer(contentSql.toString().trim());
        for (Object o : iterable) {
            if (i != 0 && separator != null) {
                subSql.append(separator);
            }

            if (o instanceof Map.Entry) {
                Map.Entry<Object, Object> mapEntry = (Map.Entry<Object, Object>) o;
                applyIndex(param, mapEntry.getKey(), i);
                applyItem(param, mapEntry.getValue(), i);
            } else {
                applyIndex(param, i, i);
                applyItem(param, o, i);
            }

            subSql.append(applyContext(contentSql, i));
            i++;
        }

        applyClose(subSql);
        return true;
    }

    private StringBuffer applyContext(StringBuffer contentSql, Integer i) {

        StringBuffer subSql = new StringBuffer();
        String sharpPattern = String.format("%s\\w+(,\\w+)?%s", PRIFIX_SHARP,
            SUBFIX);
        Pattern pattern = Pattern.compile(sharpPattern);
        Matcher matcher = pattern.matcher(contentSql);
        while (matcher.find()) {
            String res = matcher.group();

            Integer endIdx = res.indexOf(",") < 0 ? res.length() - 1 : res.indexOf(",");
            String varName = res.substring(2, endIdx);

            if (varName.trim().equalsIgnoreCase(item)) {
                res = res.replaceFirst(varName, itemizeItem(item, i));
            }

            if (varName.trim().equalsIgnoreCase(index)) {
                res = res.replaceFirst(varName, itemizeItem(index, i));
            }
            matcher.appendReplacement(subSql, res);
        }
        if (subSql.length() < 1) {
            return subSql;
        }
        matcher.appendTail(subSql);

        return subSql;
    }

    private void applyIndex(Map<String, Object> param, Object o, Integer i) {
        if (index != null) {
            param.put(itemizeItem(index, i), o);
        }
    }

    private void applyItem(Map<String, Object> param, Object obj, Integer i) {
        if (item != null) {
            param.put(itemizeItem(item, i), i);
        }
    }

    private static String itemizeItem(String item, int i) {
        return new StringBuilder(ITEM_PREFIX).append(item).append("_").append(i).toString();
    }

    private void applyOpen(StringBuffer sbSql) {
        if (open != null) {
            sbSql.append(open);
            sbSql.append(" ");
        }
    }

    private void applyClose(StringBuffer sbSql) {
        if (close != null) {
            sbSql.append(close);
            sbSql.append(" ");
        }
    }

}
