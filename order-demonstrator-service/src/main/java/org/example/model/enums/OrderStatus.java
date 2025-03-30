package org.example.model.enums;

public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static OrderStatus getDefault() {
        return PENDING;
    }
}
