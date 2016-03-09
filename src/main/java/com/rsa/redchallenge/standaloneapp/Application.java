package com.rsa.redchallenge.standaloneapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by kars2 on 3/1/16.
 */

@SpringBootApplication(scanBasePackages = {"com.rsa.redchallenge.standaloneapp"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


        // Call for initializinf queue
        Main.initializeQueue();
    }

}
