package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.RedisAccessToken;
import io.lettuce.core.ScriptOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisAccessTokenRepositoryTest {

    @Autowired
    private RedisAccessTokenRepository repository;


    @Test
    void findAccessTokenByTokenValue() {

    }

    @Test
    void save() {
        // TODO
        RedisAccessToken token = RedisAccessToken.builder()
                .tokenValue("231")
                .businessKey("business")
                .creationTime(System.currentTimeMillis())
                .expirationTime(System.currentTimeMillis() + 1000000)
                .userId("user_123")
                .claims(Map.of("scope", "read write"))
                .build();

        AccessToken savedToken = repository.save(token).block();


        System.out.println(savedToken);

        AccessToken found = repository.findAllByUserId("user_123").blockFirst();

        System.out.println("BY USER ID: " + found);

        AccessToken byId = repository.findById(savedToken.getId()).block();

        System.out.println("by id: " + byId);

        AccessToken tokenValue = repository.findAccessTokenByTokenValue(token.getTokenValue()).block();

        System.out.println("tokenValue: " + tokenValue);
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void deleteAllByUserId() {
    }

    @Test
    void deleteById() {
    }
    //todo
    @Test
    void saveAll() {
        repository.saveAll(List.of(new RedisAccessToken())).subscribe();
    }
}