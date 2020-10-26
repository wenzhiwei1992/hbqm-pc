package com.smartpc.chiyun.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml辅助类
 * @date 2016年12月2日
 */
public class XmlUtils {
	/**
     * 从xml字符串中解析参数
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> getParamsMapFromXml(String xml) throws Exception {
        Document read = DocumentHelper.parseText(xml);
        Element node = read.getRootElement();
        Map<String, String> params = null;
        if (node != null) {
        	Iterator<Element> listIter = node.elementIterator();
            params = new HashMap<String, String>();
            while (listIter.hasNext()) {
    			Element element = (Element) listIter.next();
    			params.put(element.getName(), element.getText());
    			listIter.remove();
    		}
            listIter = null;
		}
        return params;
    }

    /**
     * 从xml流中解析参数
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> getParamsMapFromXml(InputStream xml) throws Exception {
    	SAXReader saxReader = new SAXReader();
        Document read = saxReader.read(xml);
        Element node = read.getRootElement();
        Map<String, String> params = null;
        if (node != null) {
        	Iterator<Element> listIter = node.elementIterator();
            params = new HashMap<String, String>();
            while (listIter.hasNext()) {
    			Element element = (Element) listIter.next();
    			params.put(element.getName(), element.getText());
    			listIter.remove();
    		}
            listIter = null;
		}
        return params;
    }

    public static void listNodes(Element node, Map<String, String> params) {
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        if ((list == null || list.size() == 0) && !(node.getTextTrim().equals(""))) {
            if(node.getTextTrim().contains("<![CDATA[")){
                String[] split = node.getTextTrim().split("<![CDATA[");
                split[1].replaceAll("]]>", "");
                params.put(node.getName(), split[1]);
            }
        }
        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            // 对子节点进行遍历
            listNodes(e, params);
        }
    }
}
