package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class OpenRiskDetails {

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    String projectName;

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

    public String getActualStartDateInUTC() {
        return actualStartDateInUTC;
    }

    public void setActualStartDateInUTC(String actualStartDateInUTC) {
        this.actualStartDateInUTC = actualStartDateInUTC;
    }

    public String getActualEndDateInUTC() {
        return actualEndDateInUTC;
    }

    public void setActualEndDateInUTC(String actualEndDateInUTC) {
        this.actualEndDateInUTC = actualEndDateInUTC;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    String actualStartDateInUTC;

    String actualEndDateInUTC;

    String riskDescription;


}
