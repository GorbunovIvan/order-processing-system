package org.example.repository;

import org.example.Utils;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.service.CustomerService;
import org.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderRepositoryCustomImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @MockitoSpyBean
    private CustomerService customerService;
    @MockitoSpyBean
    private ProductService productService;

    @Test
    void shouldCreateAndReturnNewOrderWhenSaveWithCascading() {

        var order = Utils.generateRandomOrder(true);
        var products = order.getOrderItems().stream().map(OrderItem::getProduct).collect(Collectors.toSet());

        var orderResult = orderRepository.saveWithCascading(order);
        assertNotNull(orderResult);
        assertEquals(order, orderResult);
        assertEquals(new HashSet<>(order.getOrderItems()), new HashSet<>(orderResult.getOrderItems()));

        verify(customerService, times(1)).findOrSave(order.getCustomer());
        verify(productService, times(1)).findOrSaveAll(products);

        var orderFoundAfterOpt = orderRepository.findById(orderResult.getId());
        assertFalse(orderFoundAfterOpt.isEmpty());
        var orderFoundAfter = orderFoundAfterOpt.get();
        assertEquals(order, orderFoundAfter);
        assertEquals(new HashSet<>(order.getOrderItems()), new HashSet<>(orderFoundAfter.getOrderItems()));
    }

    @Test
    @Sql(scripts = "/setup-test-data.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldFailWithConstraintViolationExceptionOrderWhenSaveWithCascading() {

        var allOrders = orderRepository.findAll();
        assertFalse(allOrders.isEmpty());  // Order should exist in the DB!

        var orderExisting = allOrders.getFirst();
        var order = getCleanPojoOfOrder(orderExisting);

        var products = order.getOrderItems().stream().map(OrderItem::getProduct).collect(Collectors.toSet());

        final var finalOrder = order;
        assertThrows(DataIntegrityViolationException.class, () -> orderRepository.saveWithCascading(finalOrder));

        verify(customerService, times(1)).findOrSave(order.getCustomer());
        verify(productService, times(1)).findOrSaveAll(products);

        // No new orders were inserted
        var allOrdersAfter = orderRepository.findAll();
        assertEquals(allOrders.size(), allOrdersAfter.size());
    }

    @Test
    @Sql(scripts = "/setup-test-data.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldUpdateAndReturnExistingOrderWhenSaveWithCascading() {

        var allOrders = orderRepository.findAll();
        assertFalse(allOrders.isEmpty());  // Order should exist in the DB!

        var order = allOrders.getFirst();

        var products = order.getOrderItems().stream().map(OrderItem::getProduct).collect(Collectors.toSet());

        var orderResult = orderRepository.saveWithCascading(order);
        assertNotNull(orderResult);
        assertEquals(order, orderResult);

        verify(customerService, times(1)).findOrSave(order.getCustomer());
        verify(productService, times(1)).findOrSaveAll(products);

        var orderFoundAfterPersistingOpt = orderRepository.findById(order.getId());
        assertFalse(orderFoundAfterPersistingOpt.isEmpty());
        assertEquals(order, orderFoundAfterPersistingOpt.get());

        // No new orders were inserted
        var allOrdersAfter = orderRepository.findAll();
        assertEquals(allOrders.size(), allOrdersAfter.size());
    }

    // Order can have some Hibernate specifics, like Collection wrappers (e.g. PersistentBag).
    // This method returns the clean copy of an Order entity as if it came from the DTO
    private Order getCleanPojoOfOrder(Order order) {

        var customerClean = new Customer();
        customerClean.setFirstName(order.getCustomer().getFirstName());
        customerClean.setLastName(order.getCustomer().getLastName());
        customerClean.setEmail(order.getCustomer().getEmail());
        customerClean.setPhone(order.getCustomer().getPhone());

        var orderClean = new Order();
        orderClean.setCustomer(customerClean);
        orderClean.setOrderStatus(order.getOrderStatus());
        orderClean.setPaymentMethod(order.getPaymentMethod());
        orderClean.setCreatedAt(order.getCreatedAt());

        for (var orderItem : order.getOrderItems()) {

            var productClean = new Product();
            productClean.setName(orderItem.getProduct().getName());
            productClean.setArticle(orderItem.getProduct().getArticle());
            productClean.setUnit(orderItem.getProduct().getUnit());

            var orderItemClean = new OrderItem();
            orderItemClean.setProduct(productClean);
            orderItemClean.setQuantity(orderItem.getQuantity());
            orderItemClean.setPricePerUnit(orderItem.getPricePerUnit());

            orderClean.addOrderItem(orderItemClean);
        }

        return orderClean;
    }
}