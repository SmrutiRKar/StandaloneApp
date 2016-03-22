package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.UserSession;
import com.rsa.redchallenge.standaloneapp.utils.AzureUtils;
import com.rsa.redchallenge.standaloneapp.utils.LoginLogoutHelper;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by anjana on 3/9/16.
 */
public class SecurityParser {

    private static final Log log = LogFactory.getLog(SecurityParser.class);

    public static String performLoginCheck(String payload) throws Exception {
        JSONObject jsonObj = new JSONObject(payload);
        Map<String, Object> payloadmap = AzureUtils.jsonToMap(jsonObj);
        log.info("performing login check for payloadMap obj.." + payloadmap);
        //perform login check for SA
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserSession tempSession = mapper.readValue(payload, UserSession.class);
            log.info("performing login check for payload obj.." + tempSession);
            return checkLoginFromApplications(tempSession);
        } catch (Exception e) {
            log.error("Failed to performLogin:" + e, e);
            return mapper.writeValueAsString(new UserSession());
        }

    }

    public static String checkLoginFromApplications(UserSession tempSession) throws Exception {
        String result = "";
        if(tempSession == null || tempSession.getLoginType() == null) {
            throw new Exception("UserDetails are empty");
        }
        ObjectMapper mapper = new ObjectMapper();
        performReLoginAndUpdateJsessionId(tempSession);
       return  mapper.writeValueAsString(ApplicationConstant.sessionIdMapByApplicationUser.get(tempSession.getSaUsername()));
    }

    public static void performReLoginAndUpdateJsessionId(UserSession session) throws Exception{
        RestInteractor restInteractor =  new RestInteractor();
        String res = "";
        switch(session.getLoginType()){
            case "SA":
                String jsessId =  LoginLogoutHelper.loginSA(session.getSaUsername(),session.getSaPassword());
                res =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.SA_TEST_LOGIN,null,jsessId);
                if(res.equalsIgnoreCase("true"))  {
                    handleSaSessionLogin(session,jsessId);
                } else {
                    throw new Exception("Invalid credentials");
                }
                break;
            case "ARCHER" :
                String archerJsessId =  LoginLogoutHelper.loginArcher(session.getArcherUsername(), session.getArcherPassword(), session.getArcherInstance());
                if(archerJsessId!=null && !archerJsessId.isEmpty()) {
                    handleArcherSessionLogin(session,archerJsessId);
                } else {
                    throw new Exception("Invalid credentials");
                }
                break;
            case "SSO":
                break;
            case "BOTH":
                boolean archerLogin = false,saLogin = false;
                String sajsessId =  LoginLogoutHelper.loginSA(session.getSaUsername(),session.getSaPassword());
                res =  restInteractor.performGet(String.class, ApplicationConstant.SA_BASE_URL + ApplicationConstant.SA_TEST_LOGIN,null,sajsessId);
                archerJsessId =  LoginLogoutHelper.loginArcher(session.getArcherUsername(), session.getArcherPassword(), session.getArcherInstance());
                if(archerJsessId!=null && !archerJsessId.isEmpty()) {
                    archerLogin = true;
                }
                if(res.equalsIgnoreCase("true")) {
                    saLogin = true;
                }
                handleBothSessionLogin(session,sajsessId,archerJsessId,saLogin,archerLogin);
                break;
            default:
                throw new Exception("Unknown LoginType!!");
        }

    }

    public static UserSession handleSaSessionLogin(UserSession session,String saJsessionId){
        log.info("performing login check for sa successfull..");
        //valid credentials... save the session id and credentials
        UserSession oldSession = ApplicationConstant.sessionIdMapByApplicationUser.get(session.getSaUsername());
        if(oldSession == null) {
            UserSession uSession = new UserSession();
            uSession.setSaUsername(session.getSaUsername());
            uSession.setSaPassword(session.getSaPassword());
            uSession.setSaSessionId(saJsessionId);
            uSession.setSaLoginDate(new Date());
            uSession.setLoginType("SA");
            log.info("setting new Session object for user.."+uSession);
            ApplicationConstant.sessionIdMapByApplicationUser.put(session.getSaUsername(), uSession);
        } else {
            //update sa credentials
            oldSession.setSaUsername(session.getSaUsername());
            oldSession.setSaPassword(session.getSaPassword());
            oldSession.setSaSessionId(saJsessionId);
            if(oldSession.getLoginType().equalsIgnoreCase("ARCHER") || oldSession.getLoginType().equalsIgnoreCase("SSO")){
                oldSession.setLoginType("BOTH");
            } else {
                oldSession.setLoginType("SA");
            }
            log.info("updated existing session object :"+oldSession);
        }
        return oldSession;
    }

    public static UserSession handleArcherSessionLogin(UserSession session,String archerJsessId){
        log.info("performing login check for archer successfull..");
        UserSession oldSession = ApplicationConstant.sessionIdMapByApplicationUser.get(session.getSaUsername());
        if(oldSession == null) {
            UserSession uSession = new UserSession();
            uSession.setArcherUsername(session.getArcherUsername());
            uSession.setArcherPassword(session.getArcherPassword());
            uSession.setArcherInstance(session.getArcherInstance());
            uSession.setArcherSessionId(archerJsessId);
            uSession.setArcherLoginDate(new Date());
            uSession.setLoginType("ARCHER");
            uSession.setSaUsername(session.getSaUsername());
            log.info("setting new Session object for user.."+uSession);
            ApplicationConstant.sessionIdMapByApplicationUser.put(session.getSaUsername(), uSession);
        } else {
            //update sa credentials
            //oldSession.setArcherPassword(session.getArcherPassword());
            oldSession.setArcherPassword(session.getArcherPassword());
            oldSession.setArcherSessionId(archerJsessId);
            oldSession.setArcherUsername(session.getArcherUsername());
            oldSession.setArcherInstance(session.getArcherInstance());
            if(oldSession.getLoginType().equalsIgnoreCase("SA") || oldSession.getLoginType().equalsIgnoreCase("SSO")){
                oldSession.setLoginType("BOTH");
            } else {
                oldSession.setLoginType("ARCHER");
            }
            log.info("updated existing session object :"+oldSession);
        }
        return oldSession;
    }

    public static UserSession handleBothSessionLogin(UserSession session,String saJsessionId,String archerJsessId,boolean saLogin,boolean archerLogin) throws Exception{

        if(saLogin && archerLogin)  {
            log.info("performing login check for sa and archer successfull..");
            //valid credentials... save the session id and credentials
            UserSession oldSession = ApplicationConstant.sessionIdMapByApplicationUser.get(session.getSaUsername());
            if(oldSession == null) {
                UserSession uSession = new UserSession();
                uSession.setSaUsername(session.getSaUsername());
                uSession.setSaPassword(session.getSaPassword());
                uSession.setArcherUsername(session.getArcherUsername());
                uSession.setArcherPassword(session.getArcherPassword());
                uSession.setArcherInstance(session.getArcherInstance());
                uSession.setSaSessionId(saJsessionId);
                uSession.setArcherSessionId(archerJsessId);
                uSession.setSaLoginDate(new Date());
                uSession.setArcherLoginDate(new Date());
                uSession.setLoginType("BOTH");
                log.info("setting new Session object for user.."+uSession);
                ApplicationConstant.sessionIdMapByApplicationUser.put(session.getSaUsername(), uSession);
            } else {
                //update sa credentials
                oldSession.setSaUsername(session.getSaUsername());
                oldSession.setSaPassword(session.getSaPassword());
                oldSession.setSaSessionId(saJsessionId);
                oldSession.setArcherUsername(session.getArcherUsername());
                oldSession.setArcherPassword(session.getArcherPassword());
                oldSession.setArcherInstance(session.getArcherInstance());
                oldSession.setArcherSessionId(archerJsessId);
                oldSession.setSaLoginDate(new Date());
                oldSession.setArcherLoginDate(new Date());
                oldSession.setLoginType("BOTH");
                log.info("updated existing session object :"+oldSession);
            }
            return oldSession;
        }
        else if(saLogin){
            return handleSaSessionLogin(session,saJsessionId);
        }
        else if(archerLogin){
            return handleArcherSessionLogin(session,archerJsessId);
        } else {
            throw new Exception("Invalid login!");
        }
    }


//    public static void main(String[] args) throws Exception{
//        String loginReqpayload = "{\"saPassword\":\"netwitnes\",\"archerPassword\":\"\",\"archerUsername\":\"\",\"loginType\":\"SA\",\"saUsername\":\"admin\",\"ssoUsername\":\"\",\"ssoPassword\":\"\"}";
//        System.out.println(new ParseRequestFactory().performLoginCheck(loginReqpayload));
//
//
//    }

}
