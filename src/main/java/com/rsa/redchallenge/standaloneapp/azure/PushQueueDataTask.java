package com.rsa.redchallenge.standaloneapp.azure;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.ListQueuesResult;
import com.microsoft.windowsazure.services.servicebus.models.QueueInfo;
import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.AzureResponseObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Base64;

/**
 * Created by kars2 on 2/29/16.
 */
public class PushQueueDataTask {

    private static QueueInfo queueInfo = null;
    private static final Log log = LogFactory.getLog(PushQueueDataTask.class);


    public static void initQueue(AzureResponseObject responseObject) {
        Configuration config =
                ServiceBusConfiguration.configureWithSASAuthentication(
                        ApplicationConstant.AZURE_BUS_NAME,
                        ApplicationConstant.AZURE_SHARED_POLICY_NAME,
                        ApplicationConstant.AZURE_KEY,
                        ".servicebus.windows.net"
                );

        ServiceBusContract service = ServiceBusService.create(config);
        try {
            ListQueuesResult listQueuesResult = service.listQueues();
            if (listQueuesResult.getItems().size() == 0) {
                queueInfo = new QueueInfo(ApplicationConstant.AZURE_RESPONSE_QUEUE);
                CreateQueueResult result = service.createQueue(queueInfo);
            }
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
        }
        sendMessage(service, responseObject);
    }

    public static void sendMessage(ServiceBusContract service, AzureResponseObject response) {
        // Create message, passing a string message for the body.
        BrokeredMessage message = new BrokeredMessage(response.toString());
        // Send message to the queue
        try {
            log.info("sending message to response queue: "+response);
            service.sendQueueMessage(ApplicationConstant.AZURE_RESPONSE_QUEUE, message);
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
        }
    }

    public static AzureResponseObject populateResponseObject(boolean success, String message ,String jsonData) {
        AzureResponseObject responseObject = new AzureResponseObject();
        responseObject.setMessage(message==null? "" : message);
        responseObject.setSuccess(success);
        responseObject.setJsonResponse(jsonData==null? "" : jsonData);
        return responseObject;
    }

//    public static void main(String[] args){
//        //Endpoint=sb://saappservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=RtI+FcVtOsbQQv8uCfvJHyXzzMgUb1USHRENbwTD2e8=
//
//        System.out.println(GetSASToken("https://saappservicebus.servicebus.windows.net","RootManageSharedAccessKey","RtI+FcVtOsbQQv8uCfvJHyXzzMgUb1USHRENbwTD2e8="));
//        try {
//            System.out.println("old decoded is "+ URLDecoder.decode("https%3A%2F%2FSMT-SERVICE-BUS-1.servicebus.windows.net", "UTF-8"));
//        }catch(Exception ex){
//            System.out.println(ex);
//        }
//
//    }

    private static String GetSASToken(String resourceUri, String keyName, String key) {
        long epoch = System.currentTimeMillis() / 1000L;
        int week = 60 * 60 * 24 * 7*24;
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
