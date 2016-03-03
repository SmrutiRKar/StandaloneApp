package com.azure.models;

import com.azure.models.rest.LoginLogoutRestHandler;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import java.util.Timer;

/**
 * Created by kars2 on 2/29/16.
 */
public class Main implements Daemon{

    private static Timer timer = null;

    public static void initializeQueue() {
        //new QueueBuilder().initQueue();
        System.out.print(new LoginLogoutRestHandler().login());
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println("initializing ...");
    }

    @Override
    public void start() throws Exception {
        System.out.println("starting ...");
        initializeQueue();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopping ...");
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void destroy() {
        System.out.println("done.");
    }

}
