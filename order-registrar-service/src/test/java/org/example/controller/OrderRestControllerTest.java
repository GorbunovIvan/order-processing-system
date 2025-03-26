package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.Utils;
import org.example.model.Order;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {

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
    void shouldReturnOkWhenRegisterOrder() throws Exception {

        var order = Utils.generateRandomOrder();
        var orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(MockMvcRequestBuilders.post(baseURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk());

        verify(orderService, times(1)).publishOrder(order);
    }

    @Test
    void shouldReturnOkWithCreatedAtSetWhenRegisterOrder() throws Exception {

        var order = Utils.generateRandomOrder();
        order.setCreatedAt(null);

        var orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(MockMvcRequestBuilders.post(baseURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk());

        var orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(orderService, times(1)).publishOrder(orderCaptor.capture());

        var orderPublished = orderCaptor.getValue();
        assertNotNull(orderPublished.getCreatedAt());
        assertEquals(LocalDate.now(), orderPublished.getCreatedAt().toLocalDate());
    }
}