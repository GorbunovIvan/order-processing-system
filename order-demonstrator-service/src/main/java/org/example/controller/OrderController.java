package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;
import org.example.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") Long id) {
        log.info("Received HTTP request to find order by id: {}", id);
        var order = orderService.findById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        List<Order> orders;

        if (pageNumber == null || pageSize == null) {
            log.info("Received HTTP request to find all orders");
            orders = orderService.findAll();
        } else {
            log.info("Received HTTP request to find orders in page: {}", pageNumber);
            var pageRequest = PageRequest.of(pageNumber, pageSize);
            orders = orderService.findAll(pageRequest);
        }

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/ids/{ids}")
    public ResponseEntity<List<Order>> findAllById(@PathVariable("ids") Collection<Long> ids) {
        log.info("Received HTTP request to find orders by ids: {}", ids);
        var orders = orderService.findAllById(ids);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Order>> findAllByCustomer(@PathVariable("email") String customerEmail) {
        log.info("Received HTTP request to find orders by customer with email: {}", customerEmail);
        var customer = new Customer();
        customer.setEmail(customerEmail);
        var orders = orderService.findAllByCustomer(customer);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/product/{article}")
    public ResponseEntity<List<Order>> findAllByProduct(@PathVariable("article") String productArticle) {
        log.info("Received HTTP request to find orders by product with article: {}", productArticle);
        var product = new Product();
        product.setArticle(productArticle);
        var orders = orderService.findAllByProduct(product);
        return ResponseEntity.ok().body(orders);
    }
}
