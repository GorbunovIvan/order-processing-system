package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Value("${spring.kafka.topic-orders}")
    private String topic_orders;

    public void publishOrder(@NonNull Order order) {

        log.info("Publishing order to Kafka: {}", order);

        var future = kafkaTemplate.send(topic_orders, "key", order);
        future.thenAccept(result -> log.info("Order published to Kafka: {}", result));

        kafkaTemplate.flush();
    }
}
