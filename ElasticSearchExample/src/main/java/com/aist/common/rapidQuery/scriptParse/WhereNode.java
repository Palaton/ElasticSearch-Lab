package com.aist.common.rapidQuery.scriptParse;

import java.util.Arrays;
import java.util.List;

public class WhereNode extends TrimNode {
    
    private static List<String> prefixList = Arrays.asList("AND ","OR ","AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");
    
    public WhereNode(BaseNode contents){
        super("where", null, null, null, contents);
        super.prefixesToOverride = prefixList;
    }

}
