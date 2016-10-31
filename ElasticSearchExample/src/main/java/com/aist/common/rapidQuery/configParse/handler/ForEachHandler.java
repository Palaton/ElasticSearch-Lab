package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.configParse.XPathUtils;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.ForEachNode;
import com.aist.common.rapidQuery.scriptParse.MixedNode;
import org.w3c.dom.Node;

import java.util.List;

public class ForEachHandler implements NodeHandler {
    @Override
    public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
        List<BaseNode> contents = NodeParse.parseDynamicTags(nodeToHandle);
        MixedNode mixedSqlNode = new MixedNode(contents);

        String collection = XPathUtils.getNodeAttr("@collection", nodeToHandle);
        String open = XPathUtils.getNodeAttr("@open", nodeToHandle);
        String close = XPathUtils.getNodeAttr("@close", nodeToHandle);
        String separator = XPathUtils.getNodeAttr("@separator", nodeToHandle);
        String item = XPathUtils.getNodeAttr("@item", nodeToHandle);
        String index = XPathUtils.getNodeAttr("@index", nodeToHandle);
        
        ForEachNode sqlNode = new ForEachNode(mixedSqlNode, collection, open, close, separator,
            item, index);
        targetContents.add(sqlNode);
    }
}
