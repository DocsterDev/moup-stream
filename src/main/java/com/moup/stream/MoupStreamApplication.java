package com.moup.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MoupStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoupStreamApplication.class, args);
    }

}
