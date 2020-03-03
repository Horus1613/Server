package sax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXMLbySAX {
    public static Object    readXML(String xmlFile) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            SAXHandler handler = new SAXHandler();

            saxParser.parse(xmlFile, handler);

            return handler.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
