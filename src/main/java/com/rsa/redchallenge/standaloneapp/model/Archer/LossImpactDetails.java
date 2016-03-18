package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class LossImpactDetails {

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getBusinessUnitManager() {
        return businessUnitManager;
    }

    public void setBusinessUnitManager(String businessUnitManager) {
        this.businessUnitManager = businessUnitManager;
    }

    public String getReviewTaskDescription() {
        return reviewTaskDescription;
    }

    public void setReviewTaskDescription(String reviewTaskDescription) {
        this.reviewTaskDescription = reviewTaskDescription;
    }

    String businessUnit;

    String businessUnitManager;

    String reviewTaskDescription;

}
