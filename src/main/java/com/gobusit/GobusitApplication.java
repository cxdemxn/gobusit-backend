package com.gobusit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GobusitApplication {
    public static void main(String[] args) {
        SpringApplication.run(GobusitApplication.class, args);
    }
}