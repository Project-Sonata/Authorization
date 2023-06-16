package com.odeyalo.sonata.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableReactiveFeignClients(basePackages = "com.odeyalo.sonata.suite.reactive")
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }
}
