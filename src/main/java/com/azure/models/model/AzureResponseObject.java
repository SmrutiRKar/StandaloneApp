package com.azure.models.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by kars2 on 2/29/16.
 */
public class AzureResponseObject {

    private String id;
    private Date createdAt;
    private Date updatedAt;
    private Timestamp version;
    private boolean deleted;
    private String jsonResponse;
    private String mobileId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    @Override
    public String toString() {
        return "AzureReponseObject{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", version=" + version +
                ", deleted=" + deleted +
                ", jsonResponse='" + jsonResponse + '\'' +
                ", mobileId='" + mobileId + '\'' +
                '}';
    }
}
