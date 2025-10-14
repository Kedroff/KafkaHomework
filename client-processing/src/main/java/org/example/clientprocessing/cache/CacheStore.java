package org.example.clientprocessing.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheStore {

    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();

    public Object get(String key) {
        CacheEntry cache = store.get(key);
        if (cache == null) {
            return null;
        }
        if (cache.isExpired()) {
            store.remove(key);
            return null;
        }
        return cache.getValue();
    }

    public void put(String key, Object value, long ttlMs) {
        store.put(key, new CacheEntry(value, ttlMs));
    }

    public void evict(String key) {
        store.remove(key);
    }

    public void clear() {
        store.clear();
    }
}
