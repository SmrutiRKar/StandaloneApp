package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class LossImpact {
    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    String businessUnit;

    Long lossAmountInDollars;

    public Long getLossAmountInDollars() {
        return lossAmountInDollars;
    }

    public void setLossAmountInDollars(Long lossAmountInDollars) {
        this.lossAmountInDollars = lossAmountInDollars;
    }



}
