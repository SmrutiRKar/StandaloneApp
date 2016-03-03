package com.azure.models.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by kars2 on 2/29/16.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllIncidentsDataWrapper<T> {

    private boolean success = true;
    private String message;
    private T data;
    private T children;
    private Long total;
    private String exception;
    private String exceptionMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getChildren() {
        return children;
    }

    public void setChildren(T children) {
        this.children = children;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public AllIncidentsDataWrapper(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AllIncidentsDataWrapper{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", children=" + children +
                ", total=" + total +
                ", exception='" + exception + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
