package com.odeyalo.sonata.authorization.service.login;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthentication;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthenticationProvider;
import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.authentication.manager.ReactiveAuthenticationManager;
import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component
public class ReactiveDelegatingLoginProvider implements ReactiveLoginProvider {
    private final SonataAuthenticationProvider authenticationProvider;
    private final ReactiveAuthenticationManager authenticationManager;

    public ReactiveDelegatingLoginProvider(SonataAuthenticationProvider authenticationProvider, ReactiveAuthenticationManager authenticationManager) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<AuthenticationPayload> login(String username, String password) {
        return authenticationProvider.obtainAuthentication(username, password)
                .flatMap(sonataAuthentication -> {
                    Subject subject = createSubject(sonataAuthentication);
                    return authenticationManager.createAuthentication(subject);
                })
                .doOnError(System.out::println)
                .onErrorMap(err -> true, err -> err);
    }

    private Subject createSubject(SonataAuthentication sonataAuthentication) {
        BasicUserInfo userInfo = sonataAuthentication.getUserInfo();
        return Subject.of(userInfo.id(), userInfo.username(), sonataAuthentication.getRole(), new HashSet<>(sonataAuthentication.getAuthorities()));
    }
}
