package restautant.kitchen.order.infrastructure.persistence;

import restautant.kitchen.order.domain.RedisPort;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class RedisAdapter implements RedisPort {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveSaga(String orderId, Serializable saga) {
        redisTemplate.opsForValue().set(orderId, saga);
        redisTemplate.expire(orderId, 1, TimeUnit.HOURS);
    }

    @Override
    public Serializable getSaga(String orderId) {
        Object saga = redisTemplate.opsForValue().get(orderId);
        return saga instanceof Serializable ? (Serializable) saga : null;
    }

    @Override
    public void deleteSaga(String orderId) {
        redisTemplate.delete(orderId);
    }

}
