package restaurant.order.shared.Infrastructure.optimization;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import restaurant.order.shared.cache.Cache;

import java.time.Duration;
import java.util.Set;

@Component
public class RedisCache implements Cache {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCache(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // -------------------
    // GET methods
    // -------------------

    @Override
    public <T> T get(String key, Class<T> type) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;

        try {
            return objectMapper.readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing cache value for key: " + key, e);
        }
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeRef) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;

        try {
            return objectMapper.readValue(value, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing cache value for key: " + key, e);
        }
    }

    // -------------------
    // PUT method
    // -------------------

    @Override
    public void put(String key, Object value, long ttlSeconds) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttlSeconds));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing cache value for key: " + key, e);
        }
    }

    // -------------------
    // EVICT methods
    // -------------------

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
