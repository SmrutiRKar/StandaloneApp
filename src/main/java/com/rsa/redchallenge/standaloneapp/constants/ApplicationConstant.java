package com.rsa.redchallenge.standaloneapp.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kars2 on 3/1/16.
 */
public class ApplicationConstant {

    public static HashMap<String, HashMap<String, String>> sessionIdMapByApplicationUser = new HashMap<String, HashMap<String, String>>();

    public static String SA_BASE_URL = "https://10.31.252.122/";

    // Declare all the URL's here

    public static String INCIDENT_DASHBOARD_URI = "ios/incidents/dashboard";
    public static String EDIT_INCIDENT_URI = "ios/incidents/modify";
    public static String INCIDENT_SUMMARY_URI = "ios/incidents/summary";
    public static String INCIDENT_BY_ID_URI = "ios/incidents/byid";
    public static String LIVE_INVESTIGATION_SCHEDULER_URI = "liveConnect/investigation/schedule";
    public static String LIVE_MATCHED_IPS_URI = "liveConnect/investigation/matchedIps";
}
