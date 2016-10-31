package com.aist.common.rapidQuery.configParse.handler;

import com.aist.common.exception.BusinessException;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import com.aist.common.rapidQuery.scriptParse.TextNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeParse {
    
    public static List<BaseNode> parseDynamicTags(Node node) {
        List<BaseNode> contents = new ArrayList<BaseNode>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.CDATA_SECTION_NODE
                || child.getNodeType() == Node.TEXT_NODE) {
                String data = child.getTextContent();
                BaseNode textSqlNode = new TextNode(data);
                contents.add(textSqlNode);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) { // issue #628
                String nodeName = child.getNodeName();
                NodeHandler handler = nodeHandlers(nodeName);
                if (handler == null) {
                    throw new BusinessException(
                        "Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
            }
        }
        return contents;
    }
    
    static NodeHandler nodeHandlers(String nodeName) {
        Map<String, NodeHandler> map = new HashMap<String, NodeHandler>();
        map.put("if", new IfHandler());
        map.put("choose", new ChooseHandler());
        map.put("when", new IfHandler());
        map.put("otherwise", new OtherwiseHandler());
        map.put("trim", new TrimHandler());
        map.put("where", new WhereHandler());
        map.put("foreach",new ForEachHandler());
        return map.get(nodeName);
    }

    
}
