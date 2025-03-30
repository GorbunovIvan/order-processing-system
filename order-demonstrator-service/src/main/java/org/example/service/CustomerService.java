package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer findByEmail(String email) {
        log.info("Finding customer by email: {}", email);
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    public Customer findOrSave(@NonNull Customer customer) {

        var customerSaved = findByEmail(customer.getEmail());
        if (customerSaved != null) {
            return customerSaved;
        }

        log.info("Saving new customer: {}", customer);
        return customerRepository.save(customer);
    }
}
