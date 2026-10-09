package com.netguard.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetGuardAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetGuardAiApplication.class, args);
    }
}
