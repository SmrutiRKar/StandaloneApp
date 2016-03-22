package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.model.UserSession;
import com.rsa.redchallenge.standaloneapp.parsers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kars2 on 2/29/16.
 */
@Component
public class ParseRequestFactory {

    @Autowired
    private IncidentParser incidentParser;
    @Autowired
    private ArcherDashboardParser archerDashboardParser;
    @Autowired
    private LiveConnectorParser liveConnectorParser;


    private static final Log log = LogFactory.getLog(ParseRequestFactory.class);

    public void parse(AzureRequestObject azureRequestObject) {
        String responseForQueue = " ", saSessionId = null,archerSessId = null;
        boolean success =  false;
        String errorMessage = " ";
        try {
            saSessionId =  getSessionId(azureRequestObject.getRequestUser(),"SA");
            archerSessId = getSessionId(azureRequestObject.getRequestUser(),"ARCHER");

           if(( StringUtils.isEmpty(saSessionId) && StringUtils.isEmpty(archerSessId) ) && !azureRequestObject.getRequestOperation().equals("loginCheck")){
               // User session not present!! This should not happen!
               responseForQueue = " ";
               errorMessage = "Please Login again!";
           } else {
               if (azureRequestObject.getRequestOperation().equals("dashboardJson")) {
                   responseForQueue = incidentParser.getJsonIncidents(saSessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("incidentSumary")) {
                   responseForQueue = incidentParser.getIncidentSummary(azureRequestObject.getRequestParams(), saSessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("individualIncident")) {
                   responseForQueue = incidentParser.getIncidentById(azureRequestObject.getRequestParams(), saSessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("modifyIncident")) {
                   responseForQueue = incidentParser.modifyIncident(azureRequestObject.getRequestPayload(), saSessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("loginCheck")) {
                   responseForQueue = SecurityParser.performLoginCheck(azureRequestObject.getRequestPayload());
               }  else if (azureRequestObject.getRequestOperation().equals("mainDashboard")) {
                   responseForQueue = archerDashboardParser.getDashboardReports(archerSessId,saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("archerDashboardJson")) {
                   responseForQueue = archerDashboardParser.getArcherDashboardReports(archerSessId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveConnectDashboard")) {
                   responseForQueue = liveConnectorParser.getLiveConnectDashboard(saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveIncidentSyncNow")) {
                   responseForQueue = liveConnectorParser.scheduleLiveIncidents("0",saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveIncidentSchedule")) {
                   responseForQueue = liveConnectorParser.scheduleLiveIncidents(azureRequestObject.getRequestParams(),saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveInvestigationSyncNow")) {
                   responseForQueue = liveConnectorParser.scheduleLiveInvestigation("0",saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveInvestigationSchedule")) {
                   responseForQueue = liveConnectorParser.scheduleLiveInvestigation(azureRequestObject.getRequestParams(),saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveIncidentDDown")) {
                   responseForQueue = liveConnectorParser.getLiveIncidentsDDown(azureRequestObject.getRequestParams(),saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LiveInvestigationDDown")) {
                   responseForQueue = liveConnectorParser.getLiveInvestigationDDown(azureRequestObject.getRequestParams(),saSessionId,azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("RiskDDown")) {
                   responseForQueue = ArcherDDownParser.getRiskDDown(azureRequestObject.getRequestParams(), archerSessId, azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("LossDDown")) {
                   responseForQueue = ArcherDDownParser.getLossDDown(azureRequestObject.getRequestParams(), archerSessId, azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("ComplianceDDown")) {
                   responseForQueue = ArcherDDownParser.getComplianceDDown(azureRequestObject.getRequestParams(), archerSessId, azureRequestObject.getRequestUser());
               }
               else if (azureRequestObject.getRequestOperation().equals("ThreatDDown")) {
                   responseForQueue = ArcherDDownParser.getThreatDDown(azureRequestObject.getRequestParams(), archerSessId, azureRequestObject.getRequestUser());
               }

               success =  true;
           }
            //Push data to queue
            PushQueueDataTask.initQueue(PushQueueDataTask.populateResponseObject(success,errorMessage,responseForQueue));
        } catch (Exception e) {
           log.error("failed to process request from mobile : ",e);
            String errorMsg = (e.getMessage()!= null && !e.getMessage().isEmpty()) ? e.getMessage() : "Failed to fetch data - Server Error";
            PushQueueDataTask.initQueue(PushQueueDataTask.populateResponseObject(success,errorMsg,responseForQueue));
        }
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private String getSessionId(String userName, String applicationName) throws Exception {
        String jSessionId = "";
        if(userName == null || applicationName == null) {
            return "";
        }
        UserSession session = ApplicationConstant.sessionIdMapByApplicationUser.get(userName);
        if(session != null) {
            if(applicationName.equals("ARCHER")){
                jSessionId = session.getArcherSessionId();
            } else {
                jSessionId = session.getSaSessionId();
            }
        }
        log.info("returning jSession id for user:"+userName+" for application:"+applicationName+" as :"+jSessionId);
        return jSessionId;
    }


}
