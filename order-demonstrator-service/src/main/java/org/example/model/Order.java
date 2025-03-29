package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.model.enums.OrderStatus;
import org.example.model.enums.PaymentMethod;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "orders",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "customer_id", "payment_method", "created_at" })
        },
        indexes = {
            @Index(name = "idx_orders_customer_id", columnList = "customer_id")
        }
)
@BatchSize(size = 20)
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = { "customer", "paymentMethod", "createdAt" })
@ToString
@Slf4j
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @JsonManagedReference
    @Valid
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    @ToString.Include(name = "totalSum")
    public BigDecimal getTotalSum() {
        return orderItems.stream()
                .map(OrderItem::getTotalSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    void prePersist() {
        setCreatedAt(getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
        if (getOrderStatus() == null) {
            setOrderStatus(OrderStatus.getDefault());
        }
    }

    public void setOrderItems(Collection<OrderItem> orderItems) {
        this.orderItems.forEach(item -> item.setOrder(null));
        this.orderItems.clear();
        orderItems.forEach(this::addOrderItem);
    }

    public void addOrderItem(@NotNull OrderItem orderItem) {
        if (orderItems.contains(orderItem)) {
            log.warn("Order Item (id={}) is already present in Order (id={})", orderItem.getId(), id);
            orderItem.setOrder(this);
            return;
        }
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(@NotNull OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
}
