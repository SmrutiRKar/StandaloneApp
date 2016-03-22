package com.rsa.redchallenge.standaloneapp.constants;

import com.rsa.redchallenge.standaloneapp.model.UserSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kars2 on 3/1/16.
 */
public class ApplicationConstant {

    public static HashMap<String, UserSession> sessionIdMapByApplicationUser = new HashMap<String, UserSession>();

    public static String SA_BASE_URL = "http://localhost:9191/";
    // public static String SA_BASE_URL = "https://10.31.252.122/";
    public static String AZURE_BUS_NAME = "saappservicebus";
    public static String AZURE_BUS_NAME_FULL = "saappservicebus.servicebus.windows.net";
    public static String AZURE_REQUEST_QUEUE = "requestqueue";
    public static String AZURE_RESPONSE_QUEUE = "responsequeue";
    public static String AZURE_NOTIFICATION_QUEUE = "notificationqueue";
    public static String AZURE_SHARED_POLICY_NAME = "RootManageSharedAccessKey";
    public static String AZURE_KEY = "RtI+FcVtOsbQQv8uCfvJHyXzzMgUb1USHRENbwTD2e8=";
    public static String ARCHER_BASE_URL= "http://INENSULAKRL2C/RSAarcher";

    // Declare all the URL's here

    public static String INCIDENT_DASHBOARD_URI = "ios/incidents/dashboard";
    public static String EDIT_INCIDENT_URI = "ios/incidents/modify";
    public static String INCIDENT_SUMMARY_URI = "ios/incidents/summary";
    public static String INCIDENT_BY_ID_URI = "ios/incidents/byid";
    public static String LIVE_CONNECT_DASHBOARD = "liveConnect/investigation/dashboard";
    public static String LIVE_CONNECT_MAIN_DASHBOARD = "liveConnect/investigation/mainDashboard";
    public static String LIVE_INVESTIGATION_SCHEDULER_URI = "liveConnect/investigation/schedule";
    public static String LIVE_MATCHED_IPS_URI = "liveConnect/investigation/matchedIps";
    public static String LIVE_INCIDENT_SCHEDULER_URI = "livealert/investigation/schedule";
    public static String LIVE_INCIDENT_IPS_URI = "livealert/investigation/incidents";
    public static String SA_TEST_LOGIN = "ios/incidents/testLogin";
}
