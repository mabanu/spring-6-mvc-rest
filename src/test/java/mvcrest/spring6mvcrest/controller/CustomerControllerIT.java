package mvcrest.spring6mvcrest.controller;

import jakarta.transaction.Transactional;
import mvcrest.spring6mvcrest.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Test
    void customerListTest() {

        var customers = customerController.getListCustomers();

        assertThat(customers.size()).isEqualTo(2);
    }

    @Rollback
    @Transactional
    @Test
    void emptyCustomerListTest() {
        customerRepository.deleteAll();

        var customers = customerController.getListCustomers();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void getByIdTest() {
        var customer = customerRepository.findAll().get(0);
        var dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void notFoundIdTest() {
        assertThrows(NotFoundException.class, () ->
                customerController.getCustomerById(UUID.randomUUID())
        );
    }
}