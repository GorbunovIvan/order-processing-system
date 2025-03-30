package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> registerOrder(@RequestBody @Valid Order order) {

        orderService.populateDefaultValuesForOrder(order);

        log.info("Registering order: {}", order);
        orderService.publishOrder(order);
        log.info("Order was registered successfully: {}", order);

        return ResponseEntity.ok("Order registered successfully");
    }
}
