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

    static Set<OpenRisk> openRisks = new LinkedHashSet<>();
    static Set<LossImpact> losses = new LinkedHashSet<LossImpact>();
    static Set<ComplianceRating> complianceRatings = new LinkedHashSet<>();
    static Set<TopThreat> threats = new LinkedHashSet<>();

    static Set<OpenRiskDetails> openRiskDetails = new LinkedHashSet<OpenRiskDetails>();
    static Set<LossImpactDetails> lossImpactDetails = new LinkedHashSet<LossImpactDetails>();
    static List<ComplianceRatingDetails> complianceRatingDetails = new LinkedList<ComplianceRatingDetails>();
    static Set<TopThreatDetails> topThreatDetails = new LinkedHashSet<TopThreatDetails>();

    static Set<OpenRiskDetails> riskDrillDownList = new LinkedHashSet<OpenRiskDetails>();
    static Set<LossImpactDetails> lossDrillDownList = new LinkedHashSet<LossImpactDetails>();
    static List<ComplianceRatingDetails> complianceDrillDownList = new LinkedList<ComplianceRatingDetails>();
    static List<TopThreatDetails> topThreatsDrillDownList = new LinkedList<TopThreatDetails>();

    public static void getxpathDetails(String result, RequestType requestType) throws XPathExpressionException {
        if(requestType.equals(RequestType.THREATREQUEST)){
            //mock data for threat
            TopThreat threat1 = new TopThreat("Critical",1);
            TopThreat threat2 = new TopThreat("High",3);
            TopThreat threat3 = new TopThreat("Medium",6);
            TopThreat threat4 = new TopThreat("Low",0);
            threats.add(threat1);threats.add(threat2);threats.add(threat3);threats.add(threat4);
        } else {
            InputStream is = new ByteArrayInputStream(result.getBytes());
            XPath xpath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource(is);
            NodeList nodeList = (NodeList) xpath.evaluate(xpathExpression, inputSource, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node records = nodeList.item(i);
               // System.out.println("\nOuter Element :"
                       // + records.getNodeName());
                for (int j = 0; j < records.getChildNodes().getLength(); j++) {
                    Node node = records.getChildNodes().item(j);
                    //System.out.println("\nInner Element :"
                           // + node.getNodeName());
                    if (((DeferredElementNSImpl) node).getTagName().equals("Record")) {
                        Element record = (Element) node;
                        convertNodeToOject(record, requestType);
                    }
                }
            }
        }
    }
    private static Float generateRandomRisk(){
        return new Float(5 + (int)(Math.random() * ((100 - 5) + 1)));
    }

    private static long generateRandomLoss(){
        return new Long(5000 + (int)(Math.random() * ((100000 - 5) + 1)));
    }

    private static void convertNodeToOject(Element record, RequestType requestType) {
        switch (requestType) {
            case RISKREQUEST:
                OpenRisk openRisk = new OpenRisk();
                openRisk.setProjectName(record.getChildNodes().item(0).getTextContent());
                String score = record.getChildNodes().item(1).getTextContent();
                if (score != null && !score.isEmpty() && !score.equals("0"))
                    openRisk.setBusinessRiskScore(Float.valueOf(record.getChildNodes().item(1).getTextContent()));
                else {
                    openRisk.setBusinessRiskScore(generateRandomRisk());
                }

                if (openRisks.contains(openRisk)) {
                    openRisks.remove(openRisk);
                    openRisks.add(openRisk);
                } else {
                    openRisks.add(openRisk);
                }
                break;
            case LOSSREQUEST:
                LossImpact lossImpact = new LossImpact();
                lossImpact.setBusinessUnit(record.getChildNodes().item(0).getTextContent());
                String amount = record.getChildNodes().item(1).getTextContent();
                    lossImpact.setLossAmountInDollars((amount != null && !amount.isEmpty())?Long.valueOf(amount):generateRandomLoss());
                if (losses.contains(lossImpact)) {
                    losses.remove(lossImpact);
                    losses.add(lossImpact);
                } else {
                    losses.add(lossImpact);
                }
                break;
            case COMPLIANCEREQUEST:
                ComplianceRating complianceRating1 = new ComplianceRating();
                complianceRating1.setComplianceRating("Open");
                complianceRating1.setCount(10L);
                ComplianceRating complianceRating2 = new ComplianceRating();
                complianceRating2.setComplianceRating("High");
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
                if (openRiskDetails.contains(openRiskDetail)) {
                    openRiskDetails.remove(openRiskDetail);
                    openRiskDetails.add(openRiskDetail);
                } else {
                    openRiskDetails.add(openRiskDetail);
                }
                break;
            case LOSSDRILLDOWNREQUEST:
                LossImpactDetails lossImpactDetail = new LossImpactDetails();
                lossImpactDetail.setBusinessUnit(record.getChildNodes().item(0).getTextContent());
                lossImpactDetail.setBusinessUnitManager(record.getChildNodes().item(1).getTextContent());
                lossImpactDetail.setReviewTaskDescription(record.getChildNodes().item(2).getTextContent());
                if (lossImpactDetails.contains(lossImpactDetail)) {
                    lossImpactDetails.remove(lossImpactDetail);
                    lossImpactDetails.add(lossImpactDetail);
                } else {
                    lossImpactDetails.add(lossImpactDetail);
                }
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
                    resultMap.put("Threats",threats);
                    mapper.writeValue(out, resultMap);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();

    }


    public static Map getDashboardComponents() {
        Map dashboardMap = new HashMap<String, Object>();
        try {
            dashboardMap.put("OpenRisks", openRisks);
            dashboardMap.put("Losses", losses);
            dashboardMap.put("ComplianceRatings", complianceRatings);
            dashboardMap.put("Threats",threats);
        } catch (Exception e) {
            e.printStackTrace();
            return dashboardMap;
        }
        return dashboardMap;
    }

    public static void findLossDrillDown(String requestParams) {
        riskDrillDownList.clear();
        openRiskDetails.forEach(openRiskDetail ->
        {
            if (openRiskDetail.getProjectName().equalsIgnoreCase(requestParams)) {
                riskDrillDownList.add(openRiskDetail);
            }
        });
    }

    public static void findRiskDrillDown(String requestParams) {
        lossDrillDownList.clear();
        lossImpactDetails.forEach(lossImpactDetail ->
        {
            if (lossImpactDetail.getBusinessUnit().equalsIgnoreCase(requestParams)) {
                lossDrillDownList.add(lossImpactDetail);
            }
        });

    }

    public static void findComplianceDrillDown(String requestParams) {
        complianceRatingDetails.forEach(complianceRatingDetail ->
        {
            if (complianceRatingDetail.getComplianceRating().equalsIgnoreCase(requestParams)) {
                complianceDrillDownList.add(complianceRatingDetail);
            }
        });

    }

    public static void findThreatDrillDown(String requestParams) {
        topThreatsDrillDownList.forEach(threatDetail ->
        {
           /* if (threatDetail..equalsIgnoreCase(requestParams)) {
                lossDrillDownList.add(lossImpactDetail);
            }*/
        });

    }
}
