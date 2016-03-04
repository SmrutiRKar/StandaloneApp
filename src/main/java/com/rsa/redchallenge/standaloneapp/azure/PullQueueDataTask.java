package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.model.AzureRequestObject;
import com.rsa.redchallenge.standaloneapp.model.AzureResponseObject;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by kars2 on 2/29/16.
 */
@Component("PullQueueDataTask")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PullQueueDataTask implements Runnable {

    private ServiceBusContract service;
    /*private Authentication auth;
    SecurityContext context;*/
    private static final Log log = LogFactory.getLog(PullQueueDataTask.class);

    @Autowired
    ApplicationContext applicationContext;

    public PullQueueDataTask(ServiceBusContract service) {
        this.service = service;
        //this.auth = auth;

        /*if(this.auth == null) {
            log.info("security context :" + SecurityContextHolder.getContext() + " Auth is:" + SecurityContextHolder.getContext().getAuthentication());
            SecurityUtil.escalateUserForPrivilegedDeviceConnection(SecurityContextHolder.getContext());
            log.info("security context :" + SecurityContextHolder.getContext() + " Auth is:" + SecurityContextHolder.getContext().getAuthentication());
            this.auth = SecurityContextHolder.getContext().getAuthentication();
        }*/
    }

    @Override
    public void run() {
        try {
            /*if(SecurityContextHolder.getContext().getAuthentication() == null){
                SecurityContextHolder.getContext().setAuthentication(this.auth);
                log.info("Setting security Authentication...");
                //SecurityUtil.escalateUserForPrivilegedDeviceConnection(SecurityContextHolder.getContext());
            }*/
            getMessage(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void getMessage(ServiceBusContract service) {
        try {
            ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
            opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

            while (true) {
                ReceiveQueueMessageResult resultQM =
                        service.receiveQueueMessage("TestQueue1", opts);
                BrokeredMessage message = resultQM.getValue();
                if (message != null && message.getMessageId() != null) {
                    byte[] b = new byte[2000];
                    String s = null;
                    int numRead = message.getBody().read(b);
                    String finalString = "";
                    while (-1 != numRead) {
                        s = new String(b);
                        s = s.trim();
                        finalString += s;
                        numRead = message.getBody().read(b);
                    }
                    log.info(finalString);
                    if (StringUtils.isNotBlank(finalString)) {
                        process(finalString);
                    }
                    // Remove message from queue.
                    service.deleteMessage(message);
                } else {
                    break;
                    // Added to handle no more messages.
                    // Could instead wait for more messages to be added.
                }
            }
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("Generic exception encountered: ");
            log.error(e.getMessage());
        }

    }

    private  void process(String finalString) {
        try {
            JSONObject jsonObj = new JSONObject(finalString);
            AzureRequestObject azureRequestObject = new AzureRequestObject();
            azureRequestObject.setRequestOperation(jsonObj.getString("requestOperation"));
            azureRequestObject.setRequestParams(jsonObj.getString("requestParams"));
            azureRequestObject.setRequestPayload(jsonObj.getString("requestPayload"));
            //log.info("Application context in parse in "+applicationContext +" Auth is "+SecurityContextHolder.getContext().getAuthentication());
            ParseRequestFactory.parse(azureRequestObject,applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void post(String xml) throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://msrsa.azure-mobile.net/api/completeall");
        AzureResponseObject responseObject = new AzureResponseObject();
        responseObject.setCreatedAt(new Date(System.currentTimeMillis()));
        responseObject.setUpdatedAt(new Date(System.currentTimeMillis()));
        responseObject.setVersion(new Timestamp(System.currentTimeMillis()));
        responseObject.setDeleted(Boolean.FALSE);
        responseObject.setMobileId("SAMobile1");
        responseObject.setJsonResponse(xml);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(responseObject);
        HttpEntity entity = new ByteArrayEntity(jsonInString.getBytes("UTF-8"));
        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        String result = EntityUtils.toString(response.getEntity());
    }

    public static void post() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://msrsa.azure-mobile.net/api/completeall");
        String xml = "{ \"name\":\"HIGH\",\"count\":\"0\"}";
        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String result = EntityUtils.toString(response.getEntity());
    }

}