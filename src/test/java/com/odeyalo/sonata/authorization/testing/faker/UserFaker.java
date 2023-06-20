package com.odeyalo.sonata.authorization.testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.authorization.entity.InMemoryUser;
import com.odeyalo.sonata.authorization.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class to create fake users
 */
@NoArgsConstructor
public class UserFaker {
    private static final String ROLE_USER = "ROLE";
    private final FakeUserCreator fakeUserCreator = new FakeUserCreator();
    private Faker faker = new Faker();

    public UserFaker(Faker faker) {
        this.faker = faker;
    }

    public static UserFaker getInstance() {
        return new UserFaker();
    }

    public static UserFaker createUser() {
        return getInstance().doCreateUser();
    }

    protected UserFaker doCreateUser() {
        fakeUserCreator
                .id(faker.random().nextLong())
                .businessKey(UUID.randomUUID().toString())
                .authority(new SimpleGrantedAuthority(ROLE_USER))
                .role(ROLE_USER)
                .username(faker.internet().emailAddress())
                .creationTime(faker.date().birthday(10, 50).getTime());
        return this;
    }

    public UserFaker overrideId(Long id) {
        this.fakeUserCreator.id(id);
        return this;
    }

    public UserFaker overrideBusinessKey(String businessKey) {
        this.fakeUserCreator.businessKey(businessKey);
        return this;
    }

    public UserFaker overrideCreationTime(Long creationTime) {
        this.fakeUserCreator.creationTime(creationTime);
        return this;
    }

    public UserFaker overrideUsername(String username) {
        this.fakeUserCreator.username(username);
        return this;
    }

    public UserFaker overrideAuthorities(Set<GrantedAuthority> authorities) {
        this.fakeUserCreator.authorities(authorities);
        return this;
    }

    public UserFaker overrideRole(String role) {
        this.fakeUserCreator.role(role);
        return this;
    }

    public InMemoryUser asInMemoryUser() {
        User user = get();
        return InMemoryUser.from(user);
    }

    public User get() {
        return fakeUserCreator.create();
    }

    @Getter
    static class FakeUserCreator {
        private Long id;
        private String businessKey;
        private Long creationTime;
        private String username;
        private Set<GrantedAuthority> authorities = new HashSet<>();
        private String role;

        public FakeUserCreator id(Long id) {
            this.id = id;
            return this;
        }

        public FakeUserCreator businessKey(String businessKey) {
            this.businessKey = businessKey;
            return this;
        }

        public FakeUserCreator creationTime(Long creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public FakeUserCreator username(String username) {
            this.username = username;
            return this;
        }

        public FakeUserCreator authorities(Set<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public FakeUserCreator authority(GrantedAuthority authority) {
            this.authorities.add(authority);
            return this;
        }

        public FakeUserCreator role(String role) {
            this.role = role;
            return this;
        }

        public User create() {
            return new InMemoryUser(id, businessKey, creationTime, username, authorities, role);
        }
    }
}