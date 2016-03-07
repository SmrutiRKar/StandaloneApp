package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kars2 on 3/4/16.
 */

@Component
public class IncidentParser {

    public String getJsonIncidents(String sessionId) {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,null,sessionId);
        return result;
    }

    public String getIncidentSummary(String requestParams, String sessionId) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("requestParams", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,paramsMap,sessionId);
        return result;
    }

    public String modifyIncident(String payload, String sessionId) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("requestPayload", payload);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,paramsMap,sessionId);
        return result;
    }

    public String getIncidentById(String incidentId, String sessionId) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("incidentId", incidentId);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,paramsMap,sessionId);
        return result;
    }

}
