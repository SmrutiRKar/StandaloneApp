package com.rsa.redchallenge.standaloneapp.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsa.redchallenge.standaloneapp.model.Archer.LossImpact;
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
import java.util.*;

/**
 * Created by vishwk on 3/15/2016.
 */
public class XmlParser {

    static List<OpenRisk> openRisks = new LinkedList<OpenRisk>();
    static List<LossImpact> losses = new LinkedList<LossImpact>();

    /*List<OpenRisk> complianceList = new LinkedList<>();
    List<OpenRisk> threats = new LinkedList<>();
    */
    public static String getxpathDetails(String xpathExpression, String result, RequestType requestType) throws XPathExpressionException {
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
                    convertNodeToOject(record, requestType);
                }
            }
        }

        //   openRisks = mapper.readValue(jsonInput, mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
        return writeListToJsonArray();
    }

    private static void convertNodeToOject(Element record, RequestType requestType) {
        switch (requestType) {
            case RISKREQUEST:
                OpenRisk openRisk = new OpenRisk();
                openRisk.setProjectName(record.getChildNodes().item(0).getTextContent());
                String score = record.getChildNodes().item(1).getTextContent();
                if(score!=null && !score.isEmpty())
                    openRisk.setBusinessRiskScore(Float.valueOf(record.getChildNodes().item(1).getTextContent()));
                else
                    openRisk.setBusinessRiskScore(0F);
                openRisks.add(openRisk);
                break;
            case LOSSREQUEST:
                LossImpact lossImpact = new LossImpact();
                lossImpact.setBusinessUnit(record.getChildNodes().item(0).getTextContent());
                String amount = record.getChildNodes().item(1).getTextContent();
                if(amount!=null && !amount.isEmpty())
                    lossImpact.setLossAmountInDollars(Long.valueOf(record.getChildNodes().item(1).getTextContent()));
                else
                    lossImpact.setLossAmountInDollars(0L);
                losses.add(lossImpact);
                break;
        }
    }

    private static String writeListToJsonArray() {
        final OutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("OpenRisks",openRisks);
        resultMap.put("Losses",losses);
        try {
            mapper.writeValue(out, resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(out.toString());
        return out.toString();

    }

}
