package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class LossImpactDetails {

    String lossDescription;

    String dateOfOccurrence;

    String overAllStatus;

    public String getDateOfOccurrence() {
        return dateOfOccurrence;
    }

    public void setDateOfOccurrence(String dateOfOccurrence) {
        this.dateOfOccurrence = dateOfOccurrence;
    }

    public String getLossDescription() {
        return lossDescription;
    }

    public void setLossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
    }

    public String getOverAllStatus() {
        return overAllStatus;
    }

    public void setOverAllStatus(String overAllStatus) {
        this.overAllStatus = overAllStatus;
    }


}
