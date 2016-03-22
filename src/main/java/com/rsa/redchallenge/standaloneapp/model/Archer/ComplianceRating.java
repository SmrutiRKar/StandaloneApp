package com.rsa.redchallenge.standaloneapp.model.Archer;

/**
 * Created by vishwk on 3/18/2016.
 */
public class ComplianceRating {
    String complianceRating;
    Long count;

    public String getComplianceRating() {
        return complianceRating;
    }

    public void setComplianceRating(String complianceRating) {
        this.complianceRating = complianceRating;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplianceRating that = (ComplianceRating) o;

        return complianceRating.equals(that.complianceRating);

    }

    @Override
    public int hashCode() {
        return complianceRating.hashCode();
    }
}
