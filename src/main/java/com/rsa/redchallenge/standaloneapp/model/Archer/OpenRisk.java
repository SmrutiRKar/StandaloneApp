package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class OpenRisk {

    String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getBusinessRiskScore() {
        return businessRiskScore;
    }

    public void setBusinessRiskScore(long businessRiskScore) {
        this.businessRiskScore = businessRiskScore;
    }

    Long businessRiskScore;
}
