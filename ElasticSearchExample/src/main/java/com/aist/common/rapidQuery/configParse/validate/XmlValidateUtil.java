package com.aist.common.rapidQuery.configParse.validate;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 
 * 
 * @author ouyq
 * @version $Id: XmlValidateUtil.java, v 0.1 2016年9月19日 下午5:59:54 ouyq Exp $
 */
public class XmlValidateUtil {

    private static final Logger logger     = Logger.getLogger(XmlValidateUtil.class);

    private static final String SCHEMALANG = "http://www.w3.org/2001/XMLSchema";

    /**
     * Schema校验xml文件
     * @param xmlPath xml字符串
     * @param xsdPath xsd文件路径
     * @return xml文件是否符合xsd定义的规则
     */
    public static boolean xmlStringValidate(String xmlStr, String xsdPath) {
        boolean flag = false;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(SCHEMALANG);
            File schemaLocation = new File(xsdPath);
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            InputStream is = new ByteArrayInputStream(xmlStr.getBytes());
            Source source = new StreamSource(is);
            try {
                validator.validate(source);
                flag = true;
            } catch (SAXException ex) {
                logger.info(ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Schema校验xml文件
     * @param xmlPath xml文件路径
     * @param xsdPath xsd文件路径
     * @return xml文件是否符合xsd定义的规则
     */
    public static boolean xmlFileValidate(InputStream xmlPath, InputStream xsdPath) {
        boolean flag = false;
        try {
            SchemaFactory factory = SchemaFactory.newInstance(SCHEMALANG);
            Source xsdSource = new StreamSource(xsdPath);
            Schema schema = factory.newSchema(xsdSource);

            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlPath);

            validator.validate(source);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e);
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(XmlValidateUtil.class.getClassLoader().getClass());
        InputStream xsd = XmlValidateUtil.class.getClassLoader().getResourceAsStream("query/rapidQuery/xsd/rapidQuery.xsd");
        InputStream xml = XmlValidateUtil.class.getClassLoader().getResourceAsStream("query/rapidQuery/test.rapidQuery.xml");
        xmlFileValidate(xml,xsd);
        
    }
}
