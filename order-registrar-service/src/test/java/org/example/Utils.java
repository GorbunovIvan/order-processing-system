package org.example;

import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.jeasy.random.EasyRandom;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    private final static EasyRandom easyRandom = new EasyRandom();

    public static Order generateRandomOrder() {

        var orderItems = easyRandom.objects(OrderItem.class, 3).toList();
        for (var orderItem : orderItems) {

            var product = generateRandomProduct();
            orderItem.setProduct(product);

            orderItem.setQuantity(BigDecimal.valueOf(easyRandom.nextDouble(1000)).setScale(6, RoundingMode.HALF_UP));
            orderItem.setPricePerUnit(BigDecimal.valueOf(easyRandom.nextDouble(1000)).setScale(2, RoundingMode.HALF_UP));
        }

        var customer = generateRandomCustomer();

        var order = easyRandom.nextObject(Order.class);
        order.setOrderItems(orderItems);
        order.setCustomer(customer);

        return order;
    }

    // Products
    public static Product generateRandomProduct() {
        var product = easyRandom.nextObject(Product.class);
        product.setArticle(product.getArticle().replaceAll(" ", ""));
        if (product.getArticle().length() > 16) {
            product.setArticle(product.getArticle().substring(0, 10));
        }
        return product;
    }

    // Customers
    public static Customer generateRandomCustomer() {
        var customer = easyRandom.nextObject(Customer.class);
        customer.setEmail(customer.getEmail() + "@gmail.com");
        customer.setPhone(generateRandomPhoneNumber());
        return customer;
    }

    private static String generateRandomPhoneNumber() {
        var phone = new StringBuilder("+");
        for (int i = 0; i < 12; i++) {
            phone.append(easyRandom.nextInt(9));
        }
        return phone.toString();
    }
}
