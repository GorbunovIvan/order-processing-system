package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.model.enums.UnitOfMeasurement;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "products")
@BatchSize(size = 20)
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = "article")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 199)
    private String name;

    @NotBlank
    @Size(min = 1, max = 16)
    @NaturalId
    @Column(unique = true)
    private String article;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unit;
}
