package org.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = "email")
@ToString
public class Customer {

    @Size(min = 1, max = 99)
    private String firstName;

    @Size(min = 1, max = 99)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "\\+?[0-9]{10,15}")
    private String phone;
}
