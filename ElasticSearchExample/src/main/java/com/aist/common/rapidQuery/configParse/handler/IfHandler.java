package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.configParse.XPathUtils;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.IfNode;
import com.aist.common.rapidQuery.scriptParse.MixedNode;
import org.w3c.dom.Node;

import java.util.List;

public class IfHandler implements NodeHandler {
        public IfHandler() {
            
        }

        @Override
        public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
            List<BaseNode> contents = NodeParse.parseDynamicTags(nodeToHandle);
            MixedNode mixedSqlNode = new MixedNode(contents);
            String test = XPathUtils.getNodeAttr("@test", nodeToHandle);
            IfNode ifSqlNode = new IfNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }