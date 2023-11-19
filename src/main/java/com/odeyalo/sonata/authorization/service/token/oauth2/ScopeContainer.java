package com.odeyalo.sonata.authorization.service.token.oauth2;

import com.odeyalo.sonata.authorization.support.scope.Scope;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Container with scopes
 */
@Value
@AllArgsConstructor
@Builder
public class ScopeContainer implements Iterable<Scope> {
    @Getter(value = AccessLevel.NONE)
    List<Scope> scopes;

    public static final ScopeContainer EMPTY_SCOPE_CONTAINER = ScopeContainer.builder().build();

    public ScopeContainer(Collection<Scope> scopes) {
        Assert.notNull(scopes, "Scopes should be not null!");
        this.scopes = new ArrayList<>(scopes);
    }

    public static ScopeContainer empty() {
        return EMPTY_SCOPE_CONTAINER;
    }

    public static ScopeContainer one(Scope scope) {
        return fromCollection(Collections.singletonList(scope));
    }

    public static ScopeContainer fromCollection(Collection<Scope> scopes) {
        return new ScopeContainer(scopes);
    }

    public int size() {
        return scopes.size();
    }

    public boolean isEmpty() {
        return scopes.isEmpty();
    }

    public boolean contains(Scope o) {
        return scopes.contains(o);
    }

    public Scope get(int index) {
        return scopes.get(index);
    }

    @NotNull
    @Override
    public Iterator<Scope> iterator() {
        return scopes.iterator();
    }
}
