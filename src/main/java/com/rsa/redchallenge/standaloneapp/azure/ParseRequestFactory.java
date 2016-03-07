package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.parsers.IncidentParser;
import com.rsa.redchallenge.standaloneapp.parsers.LiveConnectorParser;
import com.rsa.redchallenge.standaloneapp.utils.LoginLogoutHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private LiveConnectorParser liveConnectorParser;
    @Autowired
    private LoginLogoutHelper loginLogoutHelper;

    public void parse(AzureRequestObject azureRequestObject) {
        String responseForQueue = "", sessionId = null;
        try {
            if (azureRequestObject.getRequestOperation().equals("dashboardJson")) {
                responseForQueue = incidentParser.getJsonIncidents(sessionId);
            } else if (azureRequestObject.getRequestOperation().equals("incidentSumary")) {
                responseForQueue = incidentParser.getIncidentSummary(azureRequestObject.getRequestParams(), sessionId);
            } else if (azureRequestObject.getRequestOperation().equals("individualIncident")) {
                responseForQueue = incidentParser.getIncidentById(azureRequestObject.getRequestParams(), sessionId);
            } else if (azureRequestObject.getRequestOperation().equals("modifyIncident")) {
                responseForQueue = incidentParser.modifyIncident(azureRequestObject.getRequestPayload(), sessionId);
            }

            //Push data to queue
            PushQueueDataTask.initQueue(responseForQueue);
        } catch (Exception e) {
            e.printStackTrace();
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

    private String getSessionId(String userName, String applicationName) {
        if(userName == null || applicationName == null) {
            return "";
        }
        String jSessionId = ApplicationConstant.sessionIdMapByApplicationUser.get(userName).get(applicationName);
        if(jSessionId == null) {
            try {
                jSessionId = loginLogoutHelper.loginSA(userName, "netwitness");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jSessionId;
    }
}
