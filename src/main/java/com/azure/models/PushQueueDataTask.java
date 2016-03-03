package com.azure.models;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.ListQueuesResult;
import com.microsoft.windowsazure.services.servicebus.models.QueueInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Base64;
import com.azure.models.model.AzureResponseObject;

/**
 * Created by kars2 on 2/29/16.
 */
public class PushQueueDataTask {

    private static QueueInfo queueInfo = null;
    private static final Log log = LogFactory.getLog(PushQueueDataTask.class);


    public static void initQueue(String params) {
        Configuration config =
                ServiceBusConfiguration.configureWithSASAuthentication(
                        "SMT-SERVICE-BUS-1",
                        "RootManageSharedAccessKey",
                        "q+4rYIBoEPNICYMBV2roAP5XWhlT0hJaCD/zcZ9nwps=",
                        ".servicebus.windows.net"
                );

        ServiceBusContract service = ServiceBusService.create(config);
        try {
            ListQueuesResult listQueuesResult = service.listQueues();
            if (listQueuesResult.getItems().size() == 0) {
                queueInfo = new QueueInfo("TestQueue");
                CreateQueueResult result = service.createQueue(queueInfo);
            }
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
            // System.exit(-1);
        }
        sendMessage(service, params);
    }

    public static void sendMessage(ServiceBusContract service, String params) {
        // Create message, passing a string message for the body.
        BrokeredMessage message = new BrokeredMessage(makeObj(params).toString());
        // Send message to the queue
        try {
            service.sendQueueMessage("TestQueue", message);
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
            System.exit(-1);
        }
    }

    public static AzureResponseObject makeObj(String params) {
        AzureResponseObject responseObject = new AzureResponseObject();
        responseObject.setCreatedAt(new Date(System.currentTimeMillis()));
        responseObject.setUpdatedAt(new Date(System.currentTimeMillis()));
        responseObject.setVersion(new Timestamp(System.currentTimeMillis()));
        responseObject.setDeleted(Boolean.FALSE);
        responseObject.setJsonResponse(params);
        return responseObject;
    }

    private static String GetSASToken(String resourceUri, String keyName, String key) {
        long epoch = System.currentTimeMillis() / 1000L;
        int week = 60 * 60 * 24 * 7;
        String expiry = Long.toString(epoch + week);

        String sasToken = null;
        try {
            String stringToSign = URLEncoder.encode(resourceUri, "UTF-8") + "\n" + expiry;
            String signature = getHMAC256(key, stringToSign);
            sasToken = "SharedAccessSignature sr=" + URLEncoder.encode(resourceUri, "UTF-8") + "&sig=" +
                    URLEncoder.encode(signature, "UTF-8") + "&se=" + expiry + "&skn=" + keyName;
        } catch (Exception e) {

            e.printStackTrace();
        }
        log.info(sasToken);

        return sasToken;

    }


    public static String getHMAC256(String key, String input) {
        Mac sha256_HMAC = null;
        String hash = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            Base64.Encoder encoder = Base64.getEncoder();

            hash = new String(encoder.encode(sha256_HMAC.doFinal(input.getBytes("UTF-8"))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
}
