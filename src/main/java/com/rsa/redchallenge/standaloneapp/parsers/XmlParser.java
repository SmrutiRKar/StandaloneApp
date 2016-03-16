package com.rsa.redchallenge.standaloneapp.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsa.redchallenge.standaloneapp.model.Archer.OpenRisk;
import com.rsa.redchallenge.standaloneapp.model.Archer.RequestType;
import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishwk on 3/15/2016.
 */
public class XmlParser {


    public static String getxpathDetails(String xpathExpression,String result,RequestType requestType) throws XPathExpressionException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new ByteArrayInputStream(result.getBytes());
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(is);
        List<OpenRisk> openRisks = new ArrayList<OpenRisk>();
        NodeList nodeList = (NodeList) xpath.evaluate(xpathExpression, inputSource, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node records = nodeList.item(i);
            System.out.println("\nOuter Element :"
                    + records.getNodeName());
            for (int j = 0; j < records.getChildNodes().getLength(); j++) {
                Node node = records.getChildNodes().item(j);
                System.out.println("\nInner Element :"
                        + node.getNodeName());
                if (((DeferredElementNSImpl) node).getTagName().equals("Record")) {
                    Element record = (Element) node;
                    OpenRisk openRisk = new OpenRisk();
                    openRisk.setProjectName(record.getChildNodes().item(1).getTextContent());
                    openRisk.setBusinessRiskScore(200+j*20);
                    openRisks.add(openRisk);
                }
            }
        }

        //   openRisks = mapper.readValue(jsonInput, mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
        return writeListToJsonArray(openRisks);
    }

    private static String writeListToJsonArray(List<OpenRisk> openRisks){
        final OutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, openRisks);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(out.toString());
        return out.toString();

    }

}
