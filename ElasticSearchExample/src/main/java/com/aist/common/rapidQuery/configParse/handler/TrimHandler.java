package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.configParse.XPathUtils;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.MixedNode;
import com.aist.common.rapidQuery.scriptParse.TrimNode;
import org.w3c.dom.Node;

import java.util.List;

public class TrimHandler implements NodeHandler {
    public TrimHandler() {

    }

    @Override
    public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {

        List<BaseNode> contents = NodeParse.parseDynamicTags(nodeToHandle);
        MixedNode mixedSqlNode = new MixedNode(contents);
        
        String prefix = XPathUtils.getNodeAttr("@prefix", nodeToHandle);
        String prefixOverrides =XPathUtils.getNodeAttr("@prefixOverrides", nodeToHandle);
        String suffix = XPathUtils.getNodeAttr("@suffix", nodeToHandle); 
        String suffixOverrides = XPathUtils.getNodeAttr("@suffixOverrides", nodeToHandle); 
        
        TrimNode ifSqlNode = new TrimNode(prefix, suffix, prefixOverrides, suffixOverrides, mixedSqlNode);
        targetContents.add(ifSqlNode);
    }
}
