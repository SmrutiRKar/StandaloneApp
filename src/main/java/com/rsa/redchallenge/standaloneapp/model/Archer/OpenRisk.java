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

    public Float getBusinessRiskScore() {
        return businessRiskScore;
    }

    public void setBusinessRiskScore(Float businessRiskScore) {
        this.businessRiskScore = businessRiskScore;
    }

    Float businessRiskScore;
}
