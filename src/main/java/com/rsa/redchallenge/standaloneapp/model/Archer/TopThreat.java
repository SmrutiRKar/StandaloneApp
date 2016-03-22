package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/18/2016.
 */
public class TopThreat {

    private String riskLevel;
    private int count;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TopThreat(String riskLevel, int count) {
        this.riskLevel = riskLevel;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopThreat topThreat = (TopThreat) o;

        return riskLevel != null ? riskLevel.equals(topThreat.riskLevel) : topThreat.riskLevel == null;

    }

    @Override
    public int hashCode() {
        return riskLevel != null ? riskLevel.hashCode() : 0;
    }
}
