package org.example.service;

import org.example.Utils;
import org.example.model.Customer;
import org.example.model.Product;
import org.example.model.enums.PaymentMethod;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Sql(scripts = "/setup-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private OrderRepository orderRepository;

    @Test
    void shouldReturnOrderWhenFindById() {
        var orderResult = orderService.findById(1L);
        assertNotNull(orderResult);
        assertEquals(1L, orderResult.getId());
        assertEquals(PaymentMethod.PAYPAL, orderResult.getPaymentMethod());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnOrdersWhenFindAll() {
        var ordersResult = orderService.findAll();
        assertNotNull(ordersResult);
        assertEquals(3, ordersResult.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnOrdersByPageWhenFindAll() {

        // Second page (since pages start from 0) with page size 1 (meaning that only 1 order must be returned)
        var pageRequest = PageRequest.of(1, 1);

        var ordersResult = orderService.findAll(pageRequest);
        assertNotNull(ordersResult);
        assertEquals(1, ordersResult.size());

        var orderFound = ordersResult.getFirst();
        assertEquals(2L, orderFound.getId());
        assertEquals(PaymentMethod.CREDIT_CARD, orderFound.getPaymentMethod());

        verify(orderRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void shouldReturnOrdersWhenFindAllById() {

        var ids = List.of(2L, 3L);

        var ordersResult = orderService.findAllById(ids);
        assertNotNull(ordersResult);
        assertEquals(2, ordersResult.size());
        assertEquals(2L, ordersResult.get(0).getId());
        assertEquals(3L, ordersResult.get(1).getId());

        verify(orderRepository, times(1)).findAllById(ids);
    }

    @Test
    void shouldReturnOrdersWhenFindAllByCustomer() {

        var customer = new Customer();
        customer.setEmail("email1@test.com");

        var ordersResult = orderService.findAllByCustomer(customer);
        assertNotNull(ordersResult);
        assertEquals(2, ordersResult.size());
        assertEquals(1L, ordersResult.get(0).getId());
        assertEquals(2L, ordersResult.get(1).getId());

        //noinspection unchecked
        verify(orderRepository, times(1)).findAll(any(Example.class));
    }

    @Test
    void shouldReturnOrdersWhenFindAllByProduct() {

        var product = new Product();
        product.setArticle("222article222");

        var ordersResult = orderService.findAllByProduct(product);
        assertNotNull(ordersResult);
        assertEquals(2, ordersResult.size());
        assertEquals(2L, ordersResult.get(0).getId());
        assertEquals(3L, ordersResult.get(1).getId());

        verify(orderRepository, times(1)).findAllByProductArticle(product.getArticle());
    }

    @Test
    @Transactional(readOnly = true)
    void shouldCreateAndReturnOrderWhenCreate() {

        var order = Utils.generateRandomOrder(true);
        var orderCreated = orderService.create(order);
        assertEquals(order, orderCreated);
        verify(orderRepository, times(1)).saveWithCascading(order);

        var orderFoundAfter = orderRepository.findById(orderCreated.getId()).orElse(null);
        assertNotNull(orderFoundAfter);
        assertEquals(orderCreated.getId(), orderFoundAfter.getId());
        assertEquals(order, orderFoundAfter);
    }
}