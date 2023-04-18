package mvcrest.spring6mvcrest.repositories;

import mvcrest.spring6mvcrest.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {
    
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void savedCustomerTest() {
        var savedCustomer =  customerRepository.save(Customer.builder()
                        .customerName("Save Test")
                .build());

        System.out.println(savedCustomer.getId());

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }
}