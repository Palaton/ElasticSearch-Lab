package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.MixedNode;
import org.w3c.dom.Node;

import java.util.List;

public class OtherwiseHandler implements NodeHandler {
        public OtherwiseHandler() {
            
        }

        @Override
        public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
            List<BaseNode> contents = NodeParse.parseDynamicTags(nodeToHandle);
            MixedNode mixedSqlNode = new MixedNode(contents);
            targetContents.add(mixedSqlNode);
        }
    }