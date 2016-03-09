package com.rsa.redchallenge.standaloneapp.model;

/**
 * Created by kars2 on 2/29/16.
 */
public class AzureRequestObject {

    private String requestOperation;
    private String requestParams;
    private String requestPayload;
    private String requestUser;

    public String getRequestOperation() {
        return requestOperation;
    }

    public void setRequestOperation(String requestOperation) {
        this.requestOperation = requestOperation;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    @Override
    public String toString() {
        return "AzureRequestObject{" +
                "requestOperation='" + requestOperation + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestPayload='" + requestPayload + '\'' +
                ", requestUser='" + requestUser + '\'' +
                '}';
    }
}
