package org.example.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode
@ToString
public class OrderItem {

    @Valid
    @NotNull
    private Product product;

    @Digits(integer = 12, fraction = 6)
    @Positive
    private BigDecimal quantity;

    @Digits(integer = 9, fraction = 2)
    @PositiveOrZero
    private BigDecimal pricePerUnit;
}
