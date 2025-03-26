package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "customers")
@BatchSize(size = 20)
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = "email")
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 99)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 99)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Email
    @NaturalId
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "\\+?[0-9]{10,15}")
    private String phone;
}
