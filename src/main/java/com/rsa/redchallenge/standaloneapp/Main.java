package com.rsa.redchallenge.standaloneapp;

import com.rsa.redchallenge.standaloneapp.azure.QueueBuilder;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Timer;

/**
 * Created by kars2 on 2/29/16.
 */
public class Main implements Daemon{


    private ApplicationContext applicationContext;

    private static Timer timer = null;
    final static Log logger = LogFactory.getLog(Main.class);

    public static void initializeQueue() {
           // applicationContext.getBean(QueueBuilder.class).initQueue();
       // System.out.print(new LoginLogoutHelper().login());
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        logger.info("initializing ...");
    }

    @Override
    public void start() throws Exception {
        logger.info("starting ...");
        initializeQueue();
    }

    @Override
    public void stop() throws Exception {
        logger.info("stopping ...");
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void destroy() {
        logger.info("done.");
    }

}
