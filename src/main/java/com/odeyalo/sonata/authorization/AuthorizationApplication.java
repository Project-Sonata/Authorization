package com.odeyalo.sonata.authorization;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import com.odeyalo.sonata.suite.reactive.config.SuiteReactiveAutoConfiguration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.util.Set;

@EnableReactiveFeignClients(basePackages = "com.odeyalo.sonata.suite.reactive")
@SpringBootApplication
@EnableDiscoveryClient
@Import(SuiteReactiveAutoConfiguration.class)
public class AuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }


    @Bean
    public ApplicationRunner runner(AccessTokenManager accessTokenManager) {
        return (args) -> {
            AccessToken token = accessTokenManager.createToken(Subject.of("1", "Odeyalo", "USER",
                    Set.of(new SimpleGrantedAuthority("read"), new SimpleGrantedAuthority("write")))).block();
            System.out.println(token.getTokenValue());

            AccessToken token2 = accessTokenManager.createToken(Subject.of("2", "Odeyalo", "USER",
                    Set.of(new SimpleGrantedAuthority("read"), new SimpleGrantedAuthority("write")))).block();
            System.out.println(token2.getTokenValue());
        };
    }
}
