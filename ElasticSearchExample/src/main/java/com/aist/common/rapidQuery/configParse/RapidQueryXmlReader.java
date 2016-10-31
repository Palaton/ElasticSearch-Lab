package com.aist.common.rapidQuery.configParse;

import com.aist.common.exception.BusinessException;
import com.aist.common.rapidQuery.configParse.config.*;
import com.aist.common.rapidQuery.configParse.handler.NodeParse;
import com.aist.common.rapidQuery.configParse.validate.XmlValidateUtil;
import com.aist.common.rapidQuery.scriptParse.BaseNode;
import org.dom4j.DocumentException;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉 功能详细描述
 * 
 * @author ouyq
 * @version 1.0.0
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class RapidQueryXmlReader {
    private static final Logger LOGGER      = LoggerFactory.getLogger(RapidQueryXmlReader.class);
    private static final String ROOT_PATH   = "/queryConfig";
    private static final String IMPORT_PATH = "/queryConfig/import";
    private static final String DEBUG       = "/queryConfig/import/@debug";
    private static final String RESOURCE    = "@resource";
    private static final String XSD_PATH    = "query/rapidQuery/xsd/rapidQuery.xsd";

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @param is
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public void loadMainFile(InputStream is) {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;// builder
        org.w3c.dom.Document doc = null;
        RapidQuerysContext instance = RapidQuerysContext.getInstance();
        try {
            db = f.newDocumentBuilder();
            doc = db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error("主配置文件解析出错", e);
        }

        String debug = XPathUtils.getNodeAttr(DEBUG, doc);
        instance.setDebug(Boolean.valueOf(debug));

        NodeList resList = XPathUtils.getNodeList(IMPORT_PATH, doc);
        Integer len = resList.getLength();
        
        if (len < 1) {
            return;
        }
        
        List<String> config = new ArrayList<String>();
        for (int i = 0; i < resList.getLength(); i++) {
            config.add(XPathUtils.getNodeAttr(RESOURCE, resList.item(i)));
        }

        instance.addAllConfigFile(config);
        loadSubFile();
    }

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public void loadSubFile() {
        RapidQuerysContext instance = RapidQuerysContext.getInstance();
        List<String> configFileList = instance.getConfigFileList();
        ClassLoader classLoader = this.getClass().getClassLoader();
        validateSubXml(configFileList, classLoader);

        for (int i = 0; i < configFileList.size(); i++) {
            String cf = configFileList.get(i);
            InputStream is = classLoader.getResourceAsStream(cf);
            try {
                parserSubXml(is);
            } catch (DocumentException e) {
                LOGGER.error("读取文件出错：" + cf, e);
            } catch (JAXBException e) {
                LOGGER.error("解析文件出错：" + cf, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("流关闭出错" + cf, e);
                }
            }
        }
    }

    private void validateSubXml(List<String> configFileList, ClassLoader classLoader) {
        InputStream xsd = classLoader.getResourceAsStream(XSD_PATH);
        InputStream is = null;
        for (int i = 0; i < configFileList.size(); i++) {
            String cf = configFileList.get(i);
            is = classLoader.getResourceAsStream(cf);
            if (!XmlValidateUtil.xmlFileValidate(is, xsd)) {
                try {
                    is.close();
                    xsd.close();
                } catch (IOException e) {
                    LOGGER.error(cf + "解析出错", e);
                }

                throw new BusinessException("验证xml文件失败。path:" + cf);
            }

            try {
                is.close();
            } catch (IOException e) {
                LOGGER.error(cf + "解析出错", e);
            }

        }
    }

    /**
     * 
     * 〈一句话功能简述〉 功能详细描述
     * 
     * @param resource
     * @throws DocumentException
     * @throws JAXBException
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public void parserSubXml(InputStream in) throws DocumentException, JAXBException {

        RapidQuerysContext instance = RapidQuerysContext.getInstance();

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;// builder
        org.w3c.dom.Document doc = null;

        try {
            db = f.newDocumentBuilder();
            doc = db.parse(in);

            NodeList nodeList = XPathUtils.getNodeList("/querys/query", doc);

            Integer len = nodeList.getLength();
            for (int i = 0; i < len; i++) {
                Node queryNode = nodeList.item(i);

                String queryId = (String) XPathUtils.getNodeAttr("@id", queryNode);
                Query query = new Query();

                query.setId(queryId);

                Node ssNode = XPathUtils.getNodeList("searchScript", queryNode).item(0);
                SearchScript searchScript = parseSearchScript(ssNode);
                query.setSearchScript(searchScript);

                //column list
                query.setColumnList(buildColumnList(queryNode));

                instance.add(query);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.info("xml file:" + ROOT_PATH, e);
        }

    }

    private SearchScript parseSearchScript(Node searchNode) {
        SearchScript ss = new SearchScript();

        String type = XPathUtils.getNodeAttr("@type", searchNode);
        if (!StringUtil.isBlank(type)) {
            ss.setType(SearchTpye.valueOf(type));
        }

        String index = XPathUtils.getNodeAttr("@index", searchNode);
        ss.setIndex(index);

        String performenceCountSql = XPathUtils.getNodeAttr("@performenceCountSql", searchNode);
        ss.setPerformenceCountSql(Boolean.valueOf(performenceCountSql));

        String cacheResult = XPathUtils.getNodeAttr("@cacheResult", searchNode);
        ss.setCacheResult(Boolean.valueOf(cacheResult));

        String cacheCount = XPathUtils.getNodeAttr("@cacheCount", searchNode);
        ss.setCacheCount(Boolean.valueOf(cacheCount));

        //解析searchScript 子元素
        List<BaseNode> list = NodeParse.parseDynamicTags(searchNode);

        ss.setNodeList(list);
        return ss;
    }

    private List<Column> buildColumnList(Node queryNode) {
        List<Column> colList = new ArrayList<Column>();
        //解析所有的column
        NodeList colNodeList = XPathUtils.getNodeList("grid/column", queryNode);
        for (int j = 0; j < colNodeList.getLength(); j++) {
            Column col = buildColumn(colNodeList.item(j));
            colList.add(col);
        }

        return colList;
    }

    private Column buildColumn(Node cNode) {
        Column col = new Column();
        String cd = XPathUtils.getNodeAttr("@customDict", cNode);
        col.setCustomDict(cd);

        String field = XPathUtils.getNodeAttr("@field", cNode);
        col.setField(field);

        String expType = XPathUtils.getNodeAttr("@expType", cNode);
        col.setExpType(expType);

        String expFmt = XPathUtils.getNodeAttr("@expFmt", cNode);
        col.setExpFmt(expFmt);

        String hidden = XPathUtils.getNodeAttr("@hidden", cNode);
        col.setHidden(Boolean.valueOf(hidden));

        String title = XPathUtils.getNodeAttr("@title", cNode);
        col.setTitle(title);

        String name = XPathUtils.getNodeAttr("@name", cNode);
        col.setName(name);

        return col;
    }

    public static void main(String[] args) throws FileNotFoundException, DocumentException,
                                           JAXBException {
        File file = new File(
            "H:\\workspace\\aist-uam-platform\\aist-core\\src\\main\\resources\\query\\rapidQuery\\test.rapidQuery.xml");
        InputStream in = new FileInputStream(file);
        RapidQueryXmlReader xmlReader = new RapidQueryXmlReader();
        xmlReader.parserSubXml(in);
    }

}
