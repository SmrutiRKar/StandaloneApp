package com.rsa.redchallenge.standaloneapp.azure;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
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

    @Autowired
    private ParseRequestFactory parseRequestFactory;

    public PullQueueDataTask(ServiceBusContract service) {
        this.service = service;
    }

    @Override
    public void run() {
        try {
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
                        service.receiveQueueMessage(ApplicationConstant.AZURE_REQUEST_QUEUE, opts);
                BrokeredMessage message = resultQM.getValue();
                if (message != null && message.getMessageId() != null) {
                    byte[] b = new byte[9000];
                    String s = null;
                    int numRead = message.getBody().read(b);
                    String finalString = "";
                    while (-1 != numRead) {
                        s = new String(b);
                        s = s.trim();
                        finalString += s;
                        numRead = message.getBody().read(b);
                    }
                    log.info("received msg from queue:"+finalString);
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
            azureRequestObject.setRequestUser(jsonObj.getString("requestUser"));
            log.info("processing the request for mobile request: "+azureRequestObject);
            parseRequestFactory.parse(azureRequestObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}