package com.aist.common.rapidQuery.scriptParse;

import com.aist.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: BoundSql.java, v 0.1 2016年5月4日 下午5:34:03 ouyq Exp $
 */
public class SqlSource {

    private List<BaseNode>       sqlNodes = new ArrayList<BaseNode>();

    private String              baseSql  = "";

    private Map<String, Object> paramMap = new HashMap<String, Object>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSource.class);
    
    public SqlSource(String baseSql, Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        this.baseSql = baseSql;
    }

    private Node buildBaseSqlNode() {
        // TODO nodeStr 要替换掉，直接拿出来的baseSql就要包含标签
        String nodeStr = "<baseSql>" + baseSql + "</baseSql>";
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;// builder
        Document docment = null;
        try {
            db = f.newDocumentBuilder();
            InputStream in = new ByteArrayInputStream(nodeStr.getBytes());
            docment = db.parse(in);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.info("baseSql:" + baseSql,e);
        }
        return docment.getChildNodes().item(0);
    }

    public String getParseSql() {
        buildRootSqlNode();
        StringBuffer sbSql = new StringBuffer();
        for (BaseNode sqlNode : sqlNodes) {
            sqlNode.apply(paramMap, sbSql);
        }
        return sbSql.toString();
    }

    private List<BaseNode> parseDynamicTags(Node node) {
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

    @SuppressWarnings("all")
    private void buildRootSqlNode() {
        Node rootNode = buildBaseSqlNode();
        sqlNodes = parseDynamicTags(rootNode);
    }

    NodeHandler nodeHandlers(String nodeName) {
        Map<String, NodeHandler> map = new HashMap<String, NodeHandler>();
        map.put("if", new IfHandler());
        map.put("choose", new ChooseHandler());
        map.put("when", new IfHandler());
        map.put("otherwise", new OtherwiseHandler());
        return map.get(nodeName);
    }

    private interface NodeHandler {
        void handleNode(Node nodeToHandle, List<BaseNode> targetContents);
    }

    private class IfHandler implements NodeHandler {
        public IfHandler() {
            
        }

        @Override
        public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
            List<BaseNode> contents = parseDynamicTags(nodeToHandle);
            MixedNode mixedSqlNode = new MixedNode(contents);
            String test = nodeToHandle.getAttributes().getNamedItem("test").getNodeValue();
            IfNode ifSqlNode = new IfNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

    private class OtherwiseHandler implements NodeHandler {
        public OtherwiseHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(Node nodeToHandle, List<BaseNode> targetContents) {
            List<BaseNode> contents = parseDynamicTags(nodeToHandle);
            MixedNode mixedSqlNode = new MixedNode(contents);
            targetContents.add(mixedSqlNode);
        }
    }

    private class ChooseHandler implements NodeHandler {
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
                NodeHandler handler = nodeHandlers(nodeName);
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
}
