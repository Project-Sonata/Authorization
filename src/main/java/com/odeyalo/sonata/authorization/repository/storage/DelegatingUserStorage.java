package com.odeyalo.sonata.authorization.repository.storage;

import com.odeyalo.sonata.authorization.entity.InMemoryUser;
import com.odeyalo.sonata.authorization.entity.User;
import com.odeyalo.sonata.authorization.repository.RepositoryType;
import com.odeyalo.sonata.authorization.repository.user.ReactiveUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Delegate the job to the first picked impl
 */
@Repository
public class DelegatingUserStorage implements UserStorage {
    private final ReactiveUserRepository<User> repository;

    public DelegatingUserStorage(List<ReactiveUserRepository<? extends User>> repositories) {
        this.repository = (ReactiveUserRepository<User>) repositories.get(0);
    }

    @NotNull
    @Override
    public Mono<PersistableUser> findById(Long id) {
        return repository.findById(id)
                .map(PersistableUser::from);
    }

    @NotNull
    @Override
    public Flux<PersistableUser> findAll() {
        return repository.findAll()
                .map(PersistableUser::from);
    }

    @NotNull
    @Override
    public Mono<PersistableUser> save(PersistableUser persistableUser) {
        User user = null;
        if (repository.getRepositoryType() == RepositoryType.IN_MEMORY) {
            user = InMemoryUser.builder()
                    .id(persistableUser.getId())
                    .businessKey(persistableUser.getBusinessKey())
                    .username(persistableUser.getUsername())
                    .creationTime(persistableUser.getCreationTime())
                    .authorities(persistableUser.getGrantedAuthorities())
                    .role(persistableUser.getRole())
                    .build();
        }
        if (user == null) {
            return Mono.error(new UnsupportedOperationException("Cannot save user!"));
        }
        return repository.save(user)
                .map(PersistableUser::from);
    }

    @NotNull
    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @NotNull
    @Override
    public Mono<PersistableUser> findUserByUsername(@NotNull String username) {
        return repository.findUserByUsername(username)
                .map(PersistableUser::from);
    }

    @NotNull
    @Override
    public Mono<Void> deleteUserByUsername(@NotNull String username) {
        return repository.deleteUserByUsername(username);
    }
}
