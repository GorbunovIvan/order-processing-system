package org.example.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.model.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode
@ToString
public class Order {

    @Valid
    @NotNull
    private Customer customer;

    @NotNull
    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    @Valid
    @NotEmpty
    private List<OrderItem> orderItems;
}
