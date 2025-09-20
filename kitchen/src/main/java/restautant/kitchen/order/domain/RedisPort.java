package restautant.kitchen.order.domain;

import java.io.Serializable;

public interface RedisPort {
    void saveSaga(String orderId, Serializable saga);

    Serializable getSaga(String orderId);

    void deleteSaga(String orderId);
}
