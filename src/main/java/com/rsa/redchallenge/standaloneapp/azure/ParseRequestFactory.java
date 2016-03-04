package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kars2 on 2/29/16.
 */
public class ParseRequestFactory {

    public static String parse(AzureRequestObject azureRequestObject, ApplicationContext applicationContext) {
        String responseString = "";
        try {
            if (azureRequestObject.getRequestOperation().equals("dashboardJson")) {
                //((AllIncidentsReport)applicationContext.getBean("AllIncidentsReport")).getJsonIncidents();
                //new AllIncidentsReport().getJsonIncidents();
            } else if (azureRequestObject.getRequestOperation().equals("incidentSumary")) {
                //((IncidentSummaryReport)applicationContext.getBean("IncidentSummaryReport")).getIncidentSummary(azureRequestObject.getRequestParams());
                // new IncidentSummaryReport().getIncidentSummary(azureRequestObject.getRequestParams());
            } else if (azureRequestObject.getRequestOperation().equals("individualIncident")) {

                // new IncidentManagemnt().getIncidentById(azureRequestObject);
            } else if (azureRequestObject.getRequestOperation().equals("modifyIncident")) {
                //((IncidentManagemnt)applicationContext.getBean("IncidentManagemnt")).modifyIncident(azureRequestObject);
                // new IncidentManagemnt().modifyIncident(azureRequestObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
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
}
