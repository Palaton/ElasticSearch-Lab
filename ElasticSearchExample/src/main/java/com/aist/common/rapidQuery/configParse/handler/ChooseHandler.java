package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.exception.BusinessException;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.ChooseNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ChooseHandler implements NodeHandler {
    public ChooseHandler() {
        // Prevent Synthetic Access
    }

    @Override
    public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
        List<BaseNode> whenSqlNodes = new ArrayList<BaseNode>();
        List<BaseNode> otherwiseSqlNodes = new ArrayList<BaseNode>();
        handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes, otherwiseSqlNodes);
        BaseNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
        ChooseNode chooseSqlNode = new ChooseNode(whenSqlNodes, defaultSqlNode);
        targetContents.add(chooseSqlNode);
    }

    private void handleWhenOtherwiseNodes(Node chooseSqlNode, List<BaseNode> ifSqlNodes,
                                          List<BaseNode> defaultSqlNodes) {
        NodeList children = chooseSqlNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String nodeName = child.getNodeName();
            NodeHandler handler = NodeParse.nodeHandlers(nodeName);
            if (handler instanceof IfHandler) {
                handler.handleNode(child, ifSqlNodes);
            } else if (handler instanceof OtherwiseHandler) {
                handler.handleNode(child, defaultSqlNodes);
            }
        }
    }

    private BaseNode getDefaultSqlNode(List<BaseNode> defaultSqlNodes) {
        BaseNode defaultSqlNode = null;
        if (defaultSqlNodes.size() == 1) {
            defaultSqlNode = defaultSqlNodes.get(0);
        } else if (defaultSqlNodes.size() > 1) {
            throw new BusinessException(
                "Too many default (otherwise) elements in choose statement.");
        }
        return defaultSqlNode;
    }
}