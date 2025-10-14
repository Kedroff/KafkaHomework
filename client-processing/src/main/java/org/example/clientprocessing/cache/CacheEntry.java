package org.example.clientprocessing.cache;

public class CacheEntry {
    private final Object value;
    private final long expireAtMs;

    public CacheEntry(Object value, long ttlMs) {
        this.value = value;
        this.expireAtMs = System.currentTimeMillis() + ttlMs;
    }

    public Object getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAtMs;
    }
}
