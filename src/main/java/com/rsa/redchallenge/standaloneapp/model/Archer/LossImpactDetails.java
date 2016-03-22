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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LossImpactDetails that = (LossImpactDetails) o;

        return businessUnit.equals(that.businessUnit);

    }

    @Override
    public int hashCode() {
        return businessUnit.hashCode();
    }
}
