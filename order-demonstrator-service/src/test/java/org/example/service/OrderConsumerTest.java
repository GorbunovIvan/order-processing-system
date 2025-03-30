package org.example.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.Mockito.*;

class OrderConsumerTest {

    private OrderConsumer orderConsumer;

    private OrderService orderService;

    private final String TOPIC_NAME = "topic-orders-test";

    @BeforeEach
    void setUp() {
        orderService = Mockito.mock();
        orderConsumer = new OrderConsumer(orderService);
    }

    @Test
    void shouldCreateOrderWhenOrderMessageListener() {
        var order = Utils.generateRandomOrder(true);
        var consumerRecord = new ConsumerRecord<>(TOPIC_NAME, 0, 0, "key", order);
        when(orderService.create(order)).thenReturn(order);
        orderConsumer.orderMessageListener(consumerRecord);
        verify(orderService, times(1)).create(order);
    }

    @Test
    void shouldDoNothingWithDataIntegrityViolationExceptionWhenOrderMessageListener() {
        var order = Utils.generateRandomOrder(true);
        var consumerRecord = new ConsumerRecord<>(TOPIC_NAME, 0, 0, "key", order);
        when(orderService.create(order)).thenThrow(DataIntegrityViolationException.class);
        orderConsumer.orderMessageListener(consumerRecord);
        verify(orderService, times(1)).create(order);
    }
}