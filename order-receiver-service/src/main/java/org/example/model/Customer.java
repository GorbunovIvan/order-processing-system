package org.example.model;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode
@ToString
public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
