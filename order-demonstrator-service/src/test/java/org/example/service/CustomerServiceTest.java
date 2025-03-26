package org.example.service;

import org.example.Utils;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockitoSpyBean
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnNullWhenFindByEmail() {
        var email = "absent@test.com";
        var customerResult = customerService.findByEmail(email);
        assertNull(customerResult);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    @Sql(scripts = "/setup-test-customers.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnCustomerWhenFindByEmail() {
        var email = "email1@test.com";  // Should exist in the DB!
        var customerResult = customerService.findByEmail(email);
        assertNotNull(customerResult);
        assertEquals(email, customerResult.getEmail());
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldCreateAndReturnNewCustomerWhenFindOrSave() {

        var customer = Utils.generateRandomCustomer(true);

        var customerResult = customerService.findOrSave(customer);
        assertNotNull(customerResult.getId());
        assertEquals(customer, customerResult);

        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @Sql(scripts = "/setup-test-customers.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnExistingCustomerWhenFindOrSave() {

        var customer = Utils.generateRandomCustomer(true);
        customer.setEmail("email1@test.com");  // Should exist in the DB!

        var customerResult = customerService.findOrSave(customer);
        assertNotNull(customerResult.getId());
        assertEquals(customer, customerResult);

        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(customerRepository, never()).save(customer);
    }
}