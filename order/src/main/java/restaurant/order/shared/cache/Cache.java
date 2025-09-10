package restaurant.order.shared.cache;

import com.fasterxml.jackson.core.type.TypeReference;

public interface Cache {
    <T> T get(String key, Class<T> type);
    <T> T get(String key, TypeReference<T> typeRef);
    void put(String key, Object value, long ttlSeconds);
    void evict(String key);
    void evictByPrefix(String prefix);
}
