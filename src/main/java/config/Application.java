package config;

import com.azure.models.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by kars2 on 3/1/16.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.print("Application's main class called");
        SpringApplication.run(Application.class, args);

        // Call for initializinf queue
        Main.initializeQueue();
    }

}
