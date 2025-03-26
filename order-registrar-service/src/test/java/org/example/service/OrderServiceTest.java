package org.example.service;

import org.example.Utils;
import org.example.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Test
    void shouldSendOrderToKafkaTemplateWhenPublishOrder() {

        var order = Utils.generateRandomOrder();

        var resultExpected = new CompletableFuture<SendResult<String, Order>>();
        when(kafkaTemplate.send("topic-orders-test", "key", order)).thenReturn(resultExpected);

        orderService.publishOrder(order);

        verify(kafkaTemplate, times(1)).send("topic-orders-test", "key", order);
        verify(kafkaTemplate, times(1)).flush();
    }
}