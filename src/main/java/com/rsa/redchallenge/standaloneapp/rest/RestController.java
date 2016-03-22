package com.rsa.redchallenge.standaloneapp.rest;

import com.rsa.redchallenge.standaloneapp.azure.ParseRequestFactory;
import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.utils.LoginLogoutHelper;
import com.rsa.redchallenge.standaloneapp.utils.RestInteractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by anjana on 3/4/16.
 */
@org.springframework.web.bind.annotation.RestController
@EnableAutoConfiguration
public class RestController {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private ParseRequestFactory parseRequestFactory;

    final static Log logger = LogFactory.getLog(RestController.class);


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String simpleTest(HttpServletRequest request) {
        return "Test message from Ios Standalone Server";
    }

    @RequestMapping(value = "/satest", method = RequestMethod.GET)
    @ResponseBody
    public String accessSATest(HttpServletRequest request) {
        System.out.println("trying to access sa server for some data...");
        try {
            String jId = new LoginLogoutHelper().loginSA("admin","netwitness");
            System.out.println("received logged in jid as"+jId);
           RestInteractor restInteractor =  new RestInteractor();
           String response =  restInteractor.performGet(String.class,"https://10.31.252.122/ajax/incident/77/INC-2112?_dc=1457076976235&id=INC-2112",null,jId);
            System.out.println("received reponse from SA server:"+response);
            System.out.println("performing logout");
            new LoginLogoutHelper().logoutSA(jId);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Test message from Ios Standalone Server";
    }

    @RequestMapping(value = "/testios", method = RequestMethod.GET)
    @ResponseBody
    public String testios(HttpServletRequest request,@RequestParam(value = "payload", required = false) String payload) {

        try {
            JSONObject jsonObj = new JSONObject(payload);
            AzureRequestObject azureRequestObject = new AzureRequestObject();
            azureRequestObject.setRequestOperation(jsonObj.getString("requestOperation"));
            azureRequestObject.setRequestParams(jsonObj.getString("requestParams"));
            azureRequestObject.setRequestPayload(jsonObj.getString("requestPayload"));
            azureRequestObject.setRequestUser(jsonObj.getString("requestUser"));
            System.out.println("processing the request for mobile request: "+azureRequestObject);
            parseRequestFactory.parse(azureRequestObject);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
            return "call successfully";
    }
}
