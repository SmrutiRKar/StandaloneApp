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

    public static String performLoginCheck(String payload) throws Exception{
        String response = "";
        JSONObject jsonObj = new JSONObject(payload);
        Map<String, Object> payloadmap = AzureUtils.jsonToMap(jsonObj);
        log.info("performing login check for payloadMap obj.."+payloadmap);
        if(((String)payloadmap.get("loginType")).equalsIgnoreCase("SA") ){
            //perform login check for SA
            ObjectMapper mapper = new ObjectMapper();
            try {
                UserSession tempSession = mapper.readValue(payload,UserSession.class);
                log.info("performing login check for payload obj.."+tempSession);
                return checkLoginFromApplications(tempSession);
            }catch(Exception e){
                log.error("Failed to performLogin:"+e,e);
                return mapper.writeValueAsString(new UserSession());
            }

        } else if(((String)payloadmap.get("loginType")).equalsIgnoreCase("ARCHER")){
            //perform login check for ARCHER
            ObjectMapper mapper = new ObjectMapper();
            try {
                UserSession tempSession = mapper.readValue(payload,UserSession.class);
                log.info("performing login check for payload obj.."+tempSession);
                return checkLoginFromApplications(tempSession);
            }catch(Exception e){
                log.error("Failed to performLogin:"+e,e);
                return mapper.writeValueAsString(new UserSession());
            }
        } else if (((String)payloadmap.get("loginType")).equalsIgnoreCase("SSO") ){
            //perform login check for SSO
        } else {
            //perform login check for BOTH
        }

        return response;
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
                    log.info("performing login check for sa successfull..");
                    //valid credentials... save the session id and credentials
                    UserSession oldSession = ApplicationConstant.sessionIdMapByApplicationUser.get(session.getSaUsername());
                    if(oldSession == null) {
                        UserSession uSession = new UserSession();
                        uSession.setSaUsername(session.getSaUsername());
                        uSession.setSaPassword(session.getSaPassword());
                        uSession.setSaSessionId(jsessId);
                        uSession.setSaLoginDate(new Date());
                        uSession.setLoginType("SA");
                        log.info("setting new Session object for user.."+uSession);
                        ApplicationConstant.sessionIdMapByApplicationUser.put(session.getSaUsername(), uSession);
                    } else {
                        //update sa credentials
                        oldSession.setSaUsername(session.getSaUsername());
                        oldSession.setSaPassword(session.getSaPassword());
                        oldSession.setSaSessionId(jsessId);
                        if(oldSession.getLoginType().equalsIgnoreCase("ARCHER") || oldSession.getLoginType().equalsIgnoreCase("SSO")){
                            oldSession.setLoginType("BOTH");
                        } else {
                            oldSession.setLoginType("SA");
                        }
                        log.info("updated existing session object :"+oldSession);
                    }
                } else {
                    throw new Exception("Invalid credentials");
                }

            case "ARCHER" :
                String archerJsessId =  LoginLogoutHelper.loginArcher(session.getArcherUsername(), session.getArcherPassword(), session.getArcherInstance());
                if(archerJsessId!=null && !archerJsessId.isEmpty()) {
                    log.info("performing login check for Archer successfull..");
                    //valid credentials... save the session id and credentials
                    UserSession oldSession = ApplicationConstant.sessionIdMapByApplicationUser.get(session.getSaUsername());
                    if(oldSession == null) {
                        UserSession uSession = new UserSession();
                        uSession.setArcherUsername(session.getArcherUsername());
                        uSession.setArcherPassword(session.getArcherPassword());
                        uSession.setArcherInstance(session.getArcherInstance());
                        uSession.setArcherSessionId(archerJsessId);
                        uSession.setArcherLoginDate(new Date());
                        uSession.setLoginType("Archer");
                        log.info("setting new Session object for user.."+uSession);
                        ApplicationConstant.sessionIdMapByApplicationUser.put(session.getSaUsername(), uSession);
                    } else {
                        //update sa credentials
                        //oldSession.setArcherPassword(session.getArcherPassword());
                        oldSession.setArcherPassword(session.getArcherPassword());
                        oldSession.setArcherSessionId(archerJsessId);
                        if(oldSession.getLoginType().equalsIgnoreCase("SA") || oldSession.getLoginType().equalsIgnoreCase("SSO")){
                            oldSession.setLoginType("BOTH");
                        } else {
                            oldSession.setLoginType("Archer");
                        }
                        log.info("updated existing session object :"+oldSession);
                    }
                } else {
                    throw new Exception("Invalid credentials");
                }
                break;
            case "SSO":
                break;
            case "BOTH":
                break;
            default:
                throw new Exception("Unknown LoginType!!");
        }

    }

//    public static void main(String[] args) throws Exception{
//        String loginReqpayload = "{\"saPassword\":\"netwitnes\",\"archerPassword\":\"\",\"archerUsername\":\"\",\"loginType\":\"SA\",\"saUsername\":\"admin\",\"ssoUsername\":\"\",\"ssoPassword\":\"\"}";
//        System.out.println(new ParseRequestFactory().performLoginCheck(loginReqpayload));
//
//
//    }

}
