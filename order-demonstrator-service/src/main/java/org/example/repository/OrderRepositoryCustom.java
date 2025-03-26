package org.example.repository;

import org.example.model.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryCustom {
    Order saveWithCascading(@NonNull Order order);
}
