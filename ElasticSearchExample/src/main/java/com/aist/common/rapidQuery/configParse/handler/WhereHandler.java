package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.MixedNode;
import com.aist.common.rapidQuery.scriptParse.WhereNode;
import org.w3c.dom.Node;

import java.util.List;

public class WhereHandler implements NodeHandler {

    @Override
    public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
        
            List<BaseNode> contents = NodeParse.parseDynamicTags(nodeToHandle);
            MixedNode mixedNode = new MixedNode(contents);
            
            WhereNode where = new WhereNode(mixedNode);
            targetContents.add(where);
    }

}
