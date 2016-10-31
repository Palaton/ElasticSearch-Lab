package com.aist.common.rapidQuery.configParse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(XPathUtils.class);

    private static XPath        xpath  = null;

    static {
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
    }

    public static String getNodeAttr(String expression, Node node) {
        Object attr = null;
        try {
            attr = xpath.evaluate(expression, node, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            LOGGER.error("没找到" + expression + "属性", e);
        }
        return attr == null ? "" : String.valueOf(attr);
    }

    public static NodeList getNodeList(String expression, Node node) {
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            LOGGER.error("没找到" + expression + "节点", e);
        }
        return nodeList;
    }
}
