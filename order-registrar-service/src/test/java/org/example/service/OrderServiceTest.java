package org.example.service;

import org.example.Utils;
import org.example.model.Order;
import org.example.model.enums.UnitOfMeasurement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    void shouldPopulateValuesWhenPopulateDefaultValuesForOrder() {

        var order = Utils.generateRandomOrder();

        order.setCreatedAt(null);
        for (var orderItem : order.getOrderItems()) {
            var product = orderItem.getProduct();
            product.setUnit(null);
        }

        orderService.populateDefaultValuesForOrder(order);

        assertNotNull(order.getCreatedAt());
        assertEquals(LocalDate.now(), order.getCreatedAt().toLocalDate());

        for (var orderItem : order.getOrderItems()) {
            var product = orderItem.getProduct();
            assertNotNull(product.getUnit());
            assertEquals(UnitOfMeasurement.getDefault(), product.getUnit());
        }
    }

    @Test
    void shouldNotPopulateValuesWhenPopulateDefaultValuesForOrder() {

        var createdAt = LocalDateTime.now().minusDays(5);

        var order = Utils.generateRandomOrder();
        order.setCreatedAt(createdAt);

        for (var orderItem : order.getOrderItems()) {
            var product = orderItem.getProduct();
            product.setUnit(UnitOfMeasurement.M3);
        }

        var createdAtExpected = createdAt.truncatedTo(ChronoUnit.SECONDS);

        orderService.populateDefaultValuesForOrder(order);

        assertEquals(createdAtExpected, order.getCreatedAt());

        for (var orderItem : order.getOrderItems()) {
            var product = orderItem.getProduct();
            assertEquals(UnitOfMeasurement.M3, product.getUnit());
        }
    }
}