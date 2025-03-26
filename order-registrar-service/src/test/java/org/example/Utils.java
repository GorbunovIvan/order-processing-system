package org.example;

import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.jeasy.random.EasyRandom;

public class Utils {

    private final static EasyRandom easyRandom = new EasyRandom();

    public static Order generateRandomOrder() {

        var orderItems = easyRandom.objects(OrderItem.class, 5).toList();
        for (var orderItem : orderItems) {
            var product = easyRandom.nextObject(Product.class);
            orderItem.setProduct(product);
        }

        var customer = easyRandom.nextObject(Customer.class);

        var order = easyRandom.nextObject(Order.class);
        order.setOrderItems(orderItems);
        order.setCustomer(customer);

        return order;
    }
}
