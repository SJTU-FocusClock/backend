package com.se.focusclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SpringBootApplication
@ComponentScan("com.se.focusclock.config")
@ComponentScan("com.se.focusclock.serviceImpl")
@RestControllerAdvice
public class FocusclockApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocusclockApplication.class, args);
    }

}
