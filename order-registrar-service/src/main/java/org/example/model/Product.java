package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.model.enums.UnitOfMeasurement;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = "article")
@ToString
public class Product {

    @Size(min = 1, max = 199)
    private String name;

    @NotBlank
    @Size(min = 1, max = 16)
    private String article;

    private UnitOfMeasurement unit;
}
