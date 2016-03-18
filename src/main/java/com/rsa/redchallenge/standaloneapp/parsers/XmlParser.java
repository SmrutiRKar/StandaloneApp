package com.rsa.redchallenge.standaloneapp.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsa.redchallenge.standaloneapp.model.Archer.*;
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


    static String xpathExpression = new String("/Records");

    static List<OpenRisk> openRisks = new LinkedList<OpenRisk>();
    static List<LossImpact> losses = new LinkedList<LossImpact>();
    static List<ComplianceRating> complianceRatings = new LinkedList<>();
    static List<OpenRisk> threats = new LinkedList<>();

    static List<OpenRiskDetails> openRiskDetails = new LinkedList<OpenRiskDetails>();
    static List<LossImpactDetails> lossImpactDetails = new LinkedList<LossImpactDetails>();
    static List<ComplianceRatingDetails> complianceRatingDetails = new LinkedList<ComplianceRatingDetails>();
    static List<TopThreatDetails> topThreatDetails = new LinkedList<TopThreatDetails>();

    static List<OpenRiskDetails> riskDrillDownList = new ArrayList<OpenRiskDetails>();
    static List<LossImpactDetails> lossDrillDownList = new ArrayList<LossImpactDetails>();
    static List<ComplianceRatingDetails> complianceDrillDownList = new LinkedList<ComplianceRatingDetails>();
    static List<TopThreatDetails> topThreatsDrillDownList = new LinkedList<TopThreatDetails>();

    public static void getxpathDetails(String result, RequestType requestType) throws XPathExpressionException {
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
    }

    private static void convertNodeToOject(Element record, RequestType requestType) {
        switch (requestType) {
            case RISKREQUEST:
                OpenRisk openRisk = new OpenRisk();
                openRisk.setProjectName(record.getChildNodes().item(0).getTextContent());
                String score = record.getChildNodes().item(1).getTextContent();
                if (score != null && !score.isEmpty())
                    openRisk.setBusinessRiskScore(Float.valueOf(record.getChildNodes().item(1).getTextContent()));
                else
                    openRisk.setBusinessRiskScore(0F);
                openRisks.add(openRisk);
                break;
            case LOSSREQUEST:
                LossImpact lossImpact = new LossImpact();
                lossImpact.setBusinessUnit(record.getChildNodes().item(0).getTextContent());
                String amount = record.getChildNodes().item(1).getTextContent();
                    lossImpact.setLossAmountInDollars((amount != null && !amount.isEmpty())?Long.valueOf(amount):0L);
                losses.add(lossImpact);
                break;
            case COMPLIANCEREQUEST:
                ComplianceRating complianceRating1 = new ComplianceRating();
                complianceRating1.setComplianceRating("open");
                complianceRating1.setCount(10L);
                ComplianceRating complianceRating2 = new ComplianceRating();
                complianceRating2.setComplianceRating("high");
                complianceRating2.setCount(1L);
                ComplianceRating complianceRating3 = new ComplianceRating();
                complianceRating3.setComplianceRating("N/A");
                complianceRating3.setCount(2L);
                complianceRatings.add(complianceRating1);
                complianceRatings.add(complianceRating2);
                complianceRatings.add(complianceRating3);
                break;
            case THREATREQUEST:
                break;
            case RISKDRILLDOWNREQUEST:
                OpenRiskDetails openRiskDetail = new OpenRiskDetails();
                openRiskDetail.setProjectName(record.getChildNodes().item(0).getTextContent());
                openRiskDetail.setActualStartDateInUTC(record.getChildNodes().item(1).getTextContent());
                openRiskDetail.setActualEndDateInUTC(record.getChildNodes().item(2).getTextContent());
                openRiskDetail.setExpectedStartDateInUTC(record.getChildNodes().item(3).getTextContent());
                openRiskDetail.setExpectedEndDateInUTC(record.getChildNodes().item(4).getTextContent());
                openRiskDetail.setRiskDescription(record.getChildNodes().item(5).getTextContent());
                openRiskDetails.add(openRiskDetail);
                break;
            case LOSSDRILLDOWNREQUEST:
                LossImpactDetails lossImpactDetail = new LossImpactDetails();
                lossImpactDetail.setBusinessUnit(record.getChildNodes().item(0).getTextContent());
                lossImpactDetail.setBusinessUnitManager(record.getChildNodes().item(1).getTextContent());
                lossImpactDetail.setReviewTaskDescription(record.getChildNodes().item(2).getTextContent());
                lossImpactDetails.add(lossImpactDetail);
                break;
            case COMPLIANCEDRILLDOWNREQUEST:
                break;
            case THREATDRILLDOWNREQUEST:
                break;
        }
    }

    public static String writeListToJsonArray(RequestType requestType) {
        final OutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            switch (requestType) {
                case RISKDRILLDOWNREQUEST:
                    mapper.writeValue(out, riskDrillDownList);
                    break;
                case LOSSDRILLDOWNREQUEST:
                    mapper.writeValue(out, lossDrillDownList);
                    break;
                case COMPLIANCEDRILLDOWNREQUEST:
                    mapper.writeValue(out, complianceDrillDownList);
                    break;
                case THREATDRILLDOWNREQUEST:
                    mapper.writeValue(out, topThreatsDrillDownList);
                    break;
                default:
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("OpenRisks", openRisks);
                    resultMap.put("Losses", losses);
                    resultMap.put("ComplianceRatings",complianceRatings);
                   // resultMap.put("Threats",threats);
                    mapper.writeValue(out, resultMap);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();

    }

    public void findLossDrillDown(String requestParams) {
        openRiskDetails.forEach(openRiskDetail ->
        {
            if (openRiskDetail.getProjectName().equalsIgnoreCase(requestParams)) {
                riskDrillDownList.add(openRiskDetail);
            }
        });
    }

    public void findRiskDrillDown(String requestParams) {
        lossImpactDetails.forEach(lossImpactDetail ->
        {
            if (lossImpactDetail.getBusinessUnit().equalsIgnoreCase(requestParams)) {
                lossDrillDownList.add(lossImpactDetail);
            }
        });

    }

    public void findComplianceDrillDown(String requestParams) {
        complianceRatingDetails.forEach(complianceRatingDetail ->
        {
            if (complianceRatingDetail.getComplianceRating().equalsIgnoreCase(requestParams)) {
                complianceDrillDownList.add(complianceRatingDetail);
            }
        });

    }

    public void findThreatDrillDown(String requestParams) {
        topThreatsDrillDownList.forEach(threatDetail ->
        {
           /* if (threatDetail..equalsIgnoreCase(requestParams)) {
                lossDrillDownList.add(lossImpactDetail);
            }*/
        });

    }
}
