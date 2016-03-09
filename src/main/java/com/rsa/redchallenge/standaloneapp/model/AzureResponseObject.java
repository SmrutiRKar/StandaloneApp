package com.rsa.redchallenge.standaloneapp.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by kars2 on 2/29/16.
 */
public class AzureResponseObject {

    private String message;
    private boolean success;
    private String jsonResponse;
    private String mobileId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
        return "AzureResponseObject{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", jsonResponse='" + jsonResponse + '\'' +
                ", mobileId='" + mobileId + '\'' +
                '}';
    }
}
