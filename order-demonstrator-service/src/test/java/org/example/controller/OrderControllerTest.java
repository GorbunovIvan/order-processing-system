package org.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.Utils;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseURI = "/api/v1/orders";

    @PostConstruct
    private void init() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    void shouldReturnOrderWhenFindById() throws Exception {

        var order = Utils.generateRandomOrder();
        var id = order.getId();

        when(orderService.findById(id)).thenReturn(order);

        var responseJson = mockMvc.perform(get(baseURI + "/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var orderResult = objectMapper.readValue(responseJson, Order.class);

        assertNotNull(orderResult);
        assertEquals(order.getId(), orderResult.getId());
        assertEquals(order, orderResult);

        verify(orderService, times(1)).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenFindById() throws Exception {

        var id = -1L;

        mockMvc.perform(get(baseURI + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).findById(id);
    }

    @Test
    void shouldReturnOrdersWhenFindAll() throws Exception {

        var orders = List.of(
                    Utils.generateRandomOrder(),
                    Utils.generateRandomOrder(),
                    Utils.generateRandomOrder()
                );

        when(orderService.findAll()).thenReturn(orders);

        var responseJson = mockMvc.perform(get(baseURI))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Order> ordersResult = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertNotNull(ordersResult);
        assertFalse(ordersResult.isEmpty());
        assertEquals(orders, ordersResult);

        verify(orderService, times(1)).findAll();
        verify(orderService, never()).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnOrdersOnPageWhenFindAll() throws Exception {

        var orders = List.of(
                    Utils.generateRandomOrder(),
                    Utils.generateRandomOrder(),
                    Utils.generateRandomOrder()
                );

        int pageNumber = 1;
        int pageSize = 3;
        var pageRequest = PageRequest.of(pageNumber, pageSize);

        when(orderService.findAll(pageRequest)).thenReturn(orders);

        var responseJson = mockMvc.perform(get(baseURI)
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Order> ordersResult = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertNotNull(ordersResult);
        assertFalse(ordersResult.isEmpty());
        assertEquals(orders, ordersResult);

        verify(orderService, never()).findAll();
        verify(orderService, times(1)).findAll(pageRequest);
    }

    @Test
    void shouldReturnOrdersWhenFindAllById() throws Exception {

        var orders = List.of(
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder()
        );

        var ids = orders.stream().map(Order::getId).collect(Collectors.toCollection(LinkedHashSet::new));
        var idsParam = ids.stream().map(String::valueOf).collect(Collectors.joining(","));

        when(orderService.findAllById(ids)).thenReturn(orders);

        var responseJson = mockMvc.perform(get(baseURI + "/ids/{ids}", idsParam))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Order> ordersResult = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertNotNull(ordersResult);
        assertFalse(ordersResult.isEmpty());
        assertEquals(orders, ordersResult);

        verify(orderService, never()).findById(any());
        verify(orderService, times(1)).findAllById(ids);
    }

    @Test
    void shouldReturnOrdersWhenFindAllByCustomer() throws Exception {

        var email = "test-test@test.com";

        var orders = List.of(
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder()
        );

        for (var order : orders) {
            order.getCustomer().setEmail(email);
        }

        var customer = new Customer();
        customer.setEmail(email);

        when(orderService.findAllByCustomer(customer)).thenReturn(orders);

        var responseJson = mockMvc.perform(get(baseURI + "/customer/{email}", email))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Order> ordersResult = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertNotNull(ordersResult);
        assertFalse(ordersResult.isEmpty());
        assertEquals(orders, ordersResult);

        verify(orderService, times(1)).findAllByCustomer(customer);
    }

    @Test
    void shouldReturnOrdersWhenFindAllByProduct() throws Exception {

        var article = "test-article-test";

        var orders = List.of(
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder(),
                Utils.generateRandomOrder()
        );

        for (var order : orders) {
            for (var orderItem : order.getOrderItems()) {
                orderItem.getProduct().setArticle(article);
            }
        }

        var product = new Product();
        product.setArticle(article);

        when(orderService.findAllByProduct(product)).thenReturn(orders);

        var responseJson = mockMvc.perform(get(baseURI + "/product/{article}", article))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Order> ordersResult = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertNotNull(ordersResult);
        assertFalse(ordersResult.isEmpty());
        assertEquals(orders, ordersResult);

        verify(orderService, times(1)).findAllByProduct(product);
    }
}