package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.service.CustomerService;
import org.example.service.ProductService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final CustomerService customerService;
    private final ProductService productService;

    @Override
    @Transactional
    public Order saveWithCascading(@NonNull Order order) {

        saveCustomer(order);
        saveOrderItems(order);

        Order orderExisting = null;
        if (order.getId() != null) {
            orderExisting = em.find(Order.class, order.getId());
            if (orderExisting != null) {
                log.info("Order with id '{}' found", order.getId());
            } else {
                log.info("Order with id '{}' NOT found", order.getId());
            }
        }

        // Updating
        if (orderExisting != null) {

            log.info("Updating existing order '{}'", order);
            orderExisting.setCustomer(order.getCustomer());
            orderExisting.setOrderItems(order.getOrderItems());
            orderExisting.setOrderStatus(order.getOrderStatus());

            return em.merge(orderExisting);
        }

        // Creating
        log.info("Creating new order '{}'", order);
        order.setId(null);
        em.persist(order);  // May fail due to Unique Constraint Violation

        return order;
    }

    private void saveCustomer(Order order) {

        var customer = order.getCustomer();
        if (customer == null) {
            return;
        }

        var customerSaved = customerService.findOrSave(customer);
        order.setCustomer(customerSaved);
    }

    private void saveOrderItems(Order order) {

        var orderItems = order.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            return;
        }

        saveProducts(orderItems);
    }

    private void saveProducts(List<OrderItem> orderItems) {

        var orderItemsByProducts = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getProduct));

        var products = orderItemsByProducts.keySet();
        var productsSaved = productService.findOrSaveAll(products);

        for (var product : productsSaved) {
            var orderItemsByProduct = orderItemsByProducts.get(product);
            orderItemsByProduct.forEach(orderItem -> orderItem.setProduct(product));
        }
    }
}
