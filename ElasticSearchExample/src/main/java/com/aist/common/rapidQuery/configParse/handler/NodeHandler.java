package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.rapidQuery.scriptParse.BaseNode;
import org.w3c.dom.Node;

import java.util.List;

public interface NodeHandler {
    void handleNode(Node nodeToHandle, List<BaseNode> targetContents);
}