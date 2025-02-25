package com.hanu.isd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.hanu.isd")
public class TetBasketProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TetBasketProjectApplication.class, args);
    }
}
