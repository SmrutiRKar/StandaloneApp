package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kars2 on 3/4/16.
 */

@Component
public class IncidentParser {

    private static final Log log = LogFactory.getLog(IncidentParser.class);

    public String getJsonIncidents(String sessionId,String user) throws Exception{
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,null,sessionId);
        if(checkIfJsessionIdExpired(result)){
            //re login to get the new jessionId...
            log.info("old expired jsession id is:"+sessionId);
            SecurityParser.performReLoginAndUpdateJsessionId(ApplicationConstant.sessionIdMapByApplicationUser.get(user));
            sessionId = ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId();
            log.info("new jsession id is:"+sessionId +" performing the request again to fetch data..");
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_DASHBOARD_URI,null,sessionId);
        }
        return result;
    }

    public String getIncidentSummary(String requestParams, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("requestParams", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_SUMMARY_URI,paramsMap,sessionId);

        return result;
    }

    public String modifyIncident(String payload, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("requestPayload", payload);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.EDIT_INCIDENT_URI,paramsMap,sessionId);
        return result;
    }

    public String getIncidentById(String incidentId, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("incidentId", incidentId);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.INCIDENT_BY_ID_URI,paramsMap,sessionId);
        return result;
    }

    public boolean checkIfJsessionIdExpired(String result){
        if(result!=null && result!= "" && result.contains("UAP.login.Application")){
            log.error("Jsession Id expired...should login to continue...");
            return true;
        }
        return false;
    }

}
