package com.rsa.redchallenge.standaloneapp.azure;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.CreateQueueResult;
import com.microsoft.windowsazure.services.servicebus.models.ListQueuesResult;
import com.microsoft.windowsazure.services.servicebus.models.QueueInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kars2 on 2/29/16.
 */
public class QueueBuilder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ApplicationContext applicationContext;

    private static QueueInfo queueInfo = null;
    private static ThreadPoolExecutor tpl = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static ScheduledExecutorService spl = Executors.newScheduledThreadPool(30);
    private static final Log log = LogFactory.getLog(PushQueueDataTask.class);

    public QueueInfo getQueueInfo() {
        if (queueInfo == null) {
            initQueue();
        }
        return queueInfo;
    }

    public void initQueue() {
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
                queueInfo = new QueueInfo("TestQueue1");
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
        //Authentication authObj = SecurityContextHolder.getContext().getAuthentication();
        //log.info("Auth Obj is "+authObj + " App context is "+applicationContext);
        /*if(authObj == null){
            SecurityUtil.escalateUserForPrivilegedDeviceConnection(SecurityContextHolder.getContext());
            log.info("security context :" + SecurityContextHolder.getContext() + " Auth is:" + SecurityContextHolder.getContext().getAuthentication());
            authObj = SecurityContextHolder.getContext().getAuthentication();
        }*/
        PullQueueDataTask pullQueueDataTask = (PullQueueDataTask)applicationContext.getBean("PullQueueDataTask",service);
        // tpl.execute(pullQueueDataTask);
        spl.scheduleAtFixedRate(pullQueueDataTask, 0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initQueue();
        log.info("Application context is "+applicationContext);
    }
}


