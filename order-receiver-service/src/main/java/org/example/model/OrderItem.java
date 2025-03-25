package org.example.model;

import lombok.*;
import org.example.model.enums.UnitOfMeasurement;

import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode
@ToString
public class OrderItem {
    private String productName;
    private UnitOfMeasurement unit;
    private BigDecimal quantity;
    private BigDecimal pricePerUnit;
}
