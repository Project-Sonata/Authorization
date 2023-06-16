package com.odeyalo.sonata.authorization.support.util;

import org.springframework.util.MultiValueMapAdapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link MultiValueMapAdapter} for {@link ConcurrentHashMap}
 * @param <K>
 * @param <V>
 */
public class MultiValueConcurrentMap<K, V> extends MultiValueMapAdapter<K, V> {

    public MultiValueConcurrentMap() {
        super(new ConcurrentHashMap<>());
    }

    public MultiValueConcurrentMap(Map<K, List<V>> targetMap) {
        super(new ConcurrentHashMap<>(targetMap));
    }
}
