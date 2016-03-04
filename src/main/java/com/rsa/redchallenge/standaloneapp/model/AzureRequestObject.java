package com.rsa.redchallenge.standaloneapp.model;

/**
 * Created by kars2 on 2/29/16.
 */
public class AzureRequestObject {

    private String requestOperation;
    private String requestParams;
    private String requestPayload;

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

    @Override
    public String toString() {
        return "RequestOperation{" +
                "requestOperation='" + requestOperation + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestPayload='" + requestPayload + '\'' +
                '}';
    }
}
