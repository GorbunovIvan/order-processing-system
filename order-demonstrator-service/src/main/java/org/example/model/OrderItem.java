package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "order_id", "product_id", "price_per_unit" })
        },
        indexes = {
                @Index(name = "idx_order_items_order_id", columnList = "order_id")
        }
)
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = { "order", "product", "pricePerUnit" })
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    @Valid
    @Setter(AccessLevel.PACKAGE)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Digits(integer = 12, fraction = 6)
    @Positive
    private BigDecimal quantity;

    @Digits(integer = 9, fraction = 2)
    @PositiveOrZero
    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @ToString.Include(name = "totalSum")
    public BigDecimal getTotalSum() {
        if (quantity == null || pricePerUnit == null
            || quantity.equals(BigDecimal.ZERO) || pricePerUnit.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(pricePerUnit);
    }
}
