package com.theimless.nannaanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NannaAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NannaAnalyticsApplication.class, args);
        printLogo();
    }

    public static void printLogo() {
        System.out.println(Constants.logo);
    }

}
