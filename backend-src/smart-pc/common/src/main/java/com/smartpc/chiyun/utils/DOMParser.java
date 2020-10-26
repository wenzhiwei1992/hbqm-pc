package com.smartpc.chiyun.utils;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

@Component
public class DOMParser {

    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    //Load and parse XML file into DOM
    public Document parse(File file) {
        Document document = null;
        try {
            //DOM parser instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //parse an XML file into a DOM tree
            document = builder.parse(file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

/*    public static void main(String[] args) throws Exception {
        FileUtil fileUtil= new FileUtil();
        MultipartFile multipartFile =null;
        File file = fileUtil.formatPlanBarPXML(multipartFile);

    }*/

}
