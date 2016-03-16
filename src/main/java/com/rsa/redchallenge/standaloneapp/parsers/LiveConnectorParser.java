package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kars2 on 3/4/16.
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LiveConnectorParser {

    private static final Log log = LogFactory.getLog(IncidentParser.class);

    public String getLiveConnectDashboard(String sessionId,String user) throws Exception{
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_CONNECT_DASHBOARD,null,sessionId);
        if(checkIfJsessionIdExpired(result,user)){
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_CONNECT_DASHBOARD,null,
                    ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId());
        }
        return result;
    }

    public String scheduleLiveInvestigation(String requestParams, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("periodInHours", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INVESTIGATION_SCHEDULER_URI,paramsMap,sessionId);
        if(checkIfJsessionIdExpired(result,user)){
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INVESTIGATION_SCHEDULER_URI,paramsMap,
                    ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId());
        }
        return result;
    }

    public String scheduleLiveIncidents(String requestParams, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("periodInHours", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INCIDENT_SCHEDULER_URI,paramsMap,sessionId);
        if(checkIfJsessionIdExpired(result,user)){
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INCIDENT_SCHEDULER_URI,paramsMap,
                    ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId());
        }
        return result;
    }

    public String getLiveIncidentsDDown(String requestParams, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("periodInHours", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INCIDENT_IPS_URI,paramsMap,sessionId);
        if(checkIfJsessionIdExpired(result,user)){
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_INCIDENT_IPS_URI,paramsMap,
                    ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId());
        }
        return result;
    }

    public String getLiveInvestigationDDown(String requestParams, String sessionId,String user) throws Exception {
        String result = "";
        RestInteractor restInteractor =  new RestInteractor();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("periodInHours", requestParams);
        result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_MATCHED_IPS_URI,paramsMap,sessionId);
        if(checkIfJsessionIdExpired(result,user)){
            result =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.LIVE_MATCHED_IPS_URI,paramsMap,
                    ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId());
        }
        return result;
    }

    public boolean checkIfJsessionIdExpired(String result,String user) throws  Exception{
        if(result!=null && result!= "" && result.contains("UAP.login.Application")){
            log.error("Jsession Id expired...should login to continue...");
            SecurityParser.performReLoginAndUpdateJsessionId(ApplicationConstant.sessionIdMapByApplicationUser.get(user));
            log.info("new jsession id is:"+ApplicationConstant.sessionIdMapByApplicationUser.get(user).getSaSessionId() +" performing the request again to fetch data..");
            return true;
        }
        return false;
    }
}
