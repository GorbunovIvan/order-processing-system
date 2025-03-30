package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.OrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findById(@NonNull Long id) {
        log.info("Find order by id: {}", id);
        return orderRepository.findById(id)
                .orElse(null);
    }

    public List<Order> findAll() {
        log.info("Find all orders");
        return orderRepository.findAll();
    }

    public List<Order> findAll(Pageable pageable) {
        log.info("Find orders in page: {}", pageable.getPageNumber());
        return orderRepository.findAll(pageable).toList();
    }

    public List<Order> findAllById(@NonNull Collection<Long> ids) {
        log.info("Find orders by ids: {}", ids);
        return orderRepository.findAllById(ids);
    }

    public List<Order> findAllByCustomer(Customer customer) {
        log.info("Find orders by customer: {}", customer);

        var orderExample = Order.builder().customer(customer).build();
        Example<Order> example = Example.of(orderExample);

        return orderRepository.findAll(example);
    }

    public List<Order> findAllByProduct(Product product) {
        var article = product.getArticle();
        log.info("Find orders by product with article: {}", article);
        return orderRepository.findAllByProductArticle(article);
    }
    
    public Order create(@NonNull Order order) {
        log.info("Creating order: {}", order);
        return orderRepository.saveWithCascading(order);
    }
}
