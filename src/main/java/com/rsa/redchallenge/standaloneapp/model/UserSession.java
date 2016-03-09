package com.rsa.redchallenge.standaloneapp.model;

import java.util.Date;

/**
 * Created by anjana on 3/8/16.
 */
public class UserSession {

    private String ssoUsername = "";
    private String ssoPassword = "";
    private String saUsername= "";
    private String saPassword= "";
    private String archerUsername= "";
    private String archerPassword= "";
    private String archerInstance= "";
    private String saSessionId= "";
    private String archerSessionId= "";
    private Date saLoginDate;
    private Date archerLoginDate;
    private String loginType= "";

    public UserSession() {

    }

    public String getSsoUsername() {
        return ssoUsername;
    }

    public void setSsoUsername(String ssoUsername) {
        this.ssoUsername = ssoUsername;
    }

    public String getSsoPassword() {
        return ssoPassword;
    }

    public void setSsoPassword(String ssoPassword) {
        this.ssoPassword = ssoPassword;
    }

    public String getSaUsername() {
        return saUsername;
    }

    public void setSaUsername(String saUsername) {
        this.saUsername = saUsername;
    }

    public String getSaPassword() {
        return saPassword;
    }

    public void setSaPassword(String saPassword) {
        this.saPassword = saPassword;
    }

    public String getArcherUsername() {
        return archerUsername;
    }

    public void setArcherUsername(String archerUsername) {
        this.archerUsername = archerUsername;
    }

    public String getArcherPassword() {
        return archerPassword;
    }

    public void setArcherPassword(String archerPassword) {
        this.archerPassword = archerPassword;
    }

    public String getArcherInstance() {
        return archerInstance;
    }

    public void setArcherInstance(String archerInstance) {
        this.archerInstance = archerInstance;
    }

    public String getSaSessionId() {
        return saSessionId;
    }

    public void setSaSessionId(String saSessionId) {
        this.saSessionId = saSessionId;
    }

    public String getArcherSessionId() {
        return archerSessionId;
    }

    public void setArcherSessionId(String archerSessionId) {
        this.archerSessionId = archerSessionId;
    }

    public Date getSaLoginDate() {
        return saLoginDate;
    }

    public void setSaLoginDate(Date saLoginDate) {
        this.saLoginDate = saLoginDate;
    }

    public Date getArcherLoginDate() {
        return archerLoginDate;
    }

    public void setArcherLoginDate(Date archerLoginDate) {
        this.archerLoginDate = archerLoginDate;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
