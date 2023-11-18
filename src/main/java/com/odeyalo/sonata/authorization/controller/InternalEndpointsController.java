package com.odeyalo.sonata.authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal")
public class InternalEndpointsController {

    @PostMapping("/oauth/token/access")
    public Mono<ResponseEntity<?>> generateAccessToken(@RequestParam("user_id") String userId,
                                                       @RequestParam("scope") String scopes) {

        return Mono.empty();
    }
}
