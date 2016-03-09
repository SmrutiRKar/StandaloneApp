package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.model.UserSession;
import com.rsa.redchallenge.standaloneapp.parsers.IncidentParser;
import com.rsa.redchallenge.standaloneapp.parsers.LiveConnectorParser;
import com.rsa.redchallenge.standaloneapp.parsers.SecurityParser;
import com.rsa.redchallenge.standaloneapp.utils.AzureUtils;
import com.rsa.redchallenge.standaloneapp.utils.LoginLogoutHelper;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jni.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by kars2 on 2/29/16.
 */
@Component
public class ParseRequestFactory {

    @Autowired
    private IncidentParser incidentParser;
    @Autowired
    private LiveConnectorParser liveConnectorParser;


    private static final Log log = LogFactory.getLog(ParseRequestFactory.class);

    public void parse(AzureRequestObject azureRequestObject) {
        String responseForQueue = "", sessionId = null;
        boolean success =  false;
        String errorMessage = "";
        try {
            sessionId =  getSessionId(azureRequestObject.getRequestUser(),"SA");
           if(StringUtils.isEmpty(sessionId) && !azureRequestObject.getRequestOperation().equals("loginCheck")){
               // User session not present!! This should not happen!
               responseForQueue = "";
               errorMessage = "Please Login again!";
           } else {
               if (azureRequestObject.getRequestOperation().equals("dashboardJson")) {
                   responseForQueue = incidentParser.getJsonIncidents(sessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("incidentSumary")) {
                   responseForQueue = incidentParser.getIncidentSummary(azureRequestObject.getRequestParams(), sessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("individualIncident")) {
                   responseForQueue = incidentParser.getIncidentById(azureRequestObject.getRequestParams(), sessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("modifyIncident")) {
                   responseForQueue = incidentParser.modifyIncident(azureRequestObject.getRequestPayload(), sessionId,azureRequestObject.getRequestUser());
               } else if (azureRequestObject.getRequestOperation().equals("loginCheck")) {
                   responseForQueue = SecurityParser.performLoginCheck(azureRequestObject.getRequestPayload());
               }
               success =  true;
           }
            //Push data to queue
            PushQueueDataTask.initQueue(PushQueueDataTask.populateResponseObject(success,errorMessage,responseForQueue));
        } catch (Exception e) {
           log.error("failed to process request from mobile : ",e);
            PushQueueDataTask.initQueue(PushQueueDataTask.populateResponseObject(success,e.getMessage(),responseForQueue));
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
            jSessionId = session.getSaSessionId();
        }
        log.info("returning jSession id for user:"+userName+" as :"+jSessionId);
        return jSessionId;
    }


}
