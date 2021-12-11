package com.crm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A class to start the application.
 */
@SpringBootApplication
public class CrmApplication {
    /**
     * main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

}
