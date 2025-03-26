package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.model.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.consumer.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "${spring.kafka.topic-orders}")
    public void orderMessageListener(ConsumerRecord<String, Order> record) {
        log.info("Consumed record with order from Kafka: {}", record);
        Order order = record.value();
        try {
            var orderCreated = orderService.create(order);
            log.info("Order created successfully: {}", orderCreated);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to persist the order: {}\n{}", order, e.getMessage());
        }
    }
}
