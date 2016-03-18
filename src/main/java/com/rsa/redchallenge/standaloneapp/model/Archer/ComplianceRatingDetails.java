package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/18/2016.
 */

public class ComplianceRatingDetails {
    String businessUnit;

    public String getComplianceRating() {
        return complianceRating;
    }

    public void setComplianceRating(String complianceRating) {
        this.complianceRating = complianceRating;
    }

    String complianceRating;

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }
}
