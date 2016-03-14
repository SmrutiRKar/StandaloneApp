package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/4/2016.
 */
public class LossImpact {
    String lossEventName;

    String lossAmountInDollars;

    public String getLossAmountInDollars() {
        return lossAmountInDollars;
    }

    public void setLossAmountInDollars(String lossAmountInDollars) {
        this.lossAmountInDollars = lossAmountInDollars;
    }

    public String getLossEventName() {
        return lossEventName;
    }

    public void setLossEventName(String lossEventName) {
        this.lossEventName = lossEventName;
    }


}
