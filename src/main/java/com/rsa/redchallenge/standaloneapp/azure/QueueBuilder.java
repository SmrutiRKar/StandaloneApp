package com.rsa.redchallenge.standaloneapp.azure;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.ListQueuesResult;
import com.microsoft.windowsazure.services.servicebus.models.QueueInfo;
import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kars2 on 2/29/16.
 */
@Component
public class QueueBuilder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ApplicationContext applicationContext;


    private static QueueInfo queueInfo = null;
    private static ScheduledExecutorService spl = Executors.newScheduledThreadPool(30);
    private static final Log log = LogFactory.getLog(QueueBuilder.class);

    public QueueInfo getQueueInfo() {
        if (queueInfo == null) {
            initQueue();
        }
        return queueInfo;
    }

    public void initQueue() {
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
                queueInfo = new QueueInfo(ApplicationConstant.AZURE_REQUEST_QUEUE);
                CreateQueueResult result = service.createQueue(queueInfo);
            }
        } catch (ServiceException e) {
            log.error("ServiceException encountered: ");
            log.error(e.getMessage());
        }
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("## applicationContext is " +applicationContext);
        PullQueueDataTask pullQueueDataTask = (PullQueueDataTask)applicationContext.getBean("PullQueueDataTask",service);
        log.info("## starting to poll the request queue every 500 ms");
        spl.scheduleAtFixedRate(pullQueueDataTask, 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("## onApplicationEvent loads");
        initQueue();
        log.info("Application context is "+applicationContext);
    }
}


