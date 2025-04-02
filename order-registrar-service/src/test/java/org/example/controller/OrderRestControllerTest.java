package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.Utils;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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

        verify(orderService, times(1)).populateDefaultValuesForOrder(order);
        verify(orderService, times(1)).publishOrder(order);
    }

    @Test
    void shouldReturnBadRequestWhenRegisterOrder() throws Exception {

        var order = Utils.generateRandomOrder();
        order.setCustomer(null);  // Should NOT be NULL
        var orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(MockMvcRequestBuilders.post(baseURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).populateDefaultValuesForOrder(any());
        verify(orderService, never()).publishOrder(any());
    }
}