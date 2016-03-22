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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpenRisk openRisk = (OpenRisk) o;

        return projectName.equals(openRisk.projectName);

    }

    @Override
    public int hashCode() {
        return projectName.hashCode();
    }
}
