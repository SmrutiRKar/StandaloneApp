package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class OpenRiskDetails {

    public String getExpectedStartDateInUTC() {
        return expectedStartDateInUTC;
    }

    public void setExpectedStartDateInUTC(String expectedStartDateInUTC) {
        this.expectedStartDateInUTC = expectedStartDateInUTC;
    }

    public String getExpectedEndDateInUTC() {
        return expectedEndDateInUTC;
    }

    public void setExpectedEndDateInUTC(String expectedEndDateInUTC) {
        this.expectedEndDateInUTC = expectedEndDateInUTC;
    }

    String expectedStartDateInUTC;

    String expectedEndDateInUTC;

    String actualStartDateInUTC;


}
