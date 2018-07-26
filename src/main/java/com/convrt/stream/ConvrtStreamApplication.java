package com.convrt.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConvrtStreamApplication {

    public static void main(String[] args) {
        System.getProperties().put("spring.http.multipart.max-file-size", "-1");
        System.getProperties().put("spring.http.multipart.max-request-size", "-1");
        SpringApplication.run(ConvrtStreamApplication.class, args);
    }

}
